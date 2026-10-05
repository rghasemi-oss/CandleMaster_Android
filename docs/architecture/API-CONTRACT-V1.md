# N-PuDo-N API Contract v1

## 1. Purpose

This document is the canonical API contract for N-PuDo-N.

Architecture:

Android / PWA
    |
    | HTTPS + JSON + Bearer JWT
    v
N-PuDo-N Backend API v1
    |
    | Supabase Service Role
    v
Supabase PostgreSQL

Clients must not directly perform business CRUD operations against Supabase after client migration.

Supabase remains the cloud system of record.
The existing Render service remains the backend deployment.
No second backend or second database is introduced.

---

## 2. Base URL

Existing Render service:

https://candlemaster-android.onrender.com

No new Render service is required.

API base path:

/api/v1

---

## 3. Authentication

All protected endpoints require:

Authorization: Bearer <Supabase access token>

The backend validates the access token through Supabase Auth.

The Supabase Service Role Key is backend-only and must never be exposed to Android, PWA, browser JavaScript, or client configuration.

Authentication failure:

HTTP 401

Response:

{
  "success": false,
  "error": "Invalid authentication token"
}

Authentication and role authorization are separate concerns.

---

## 4. Response Envelope

Successful object response:

{
  "success": true,
  "data": {}
}

Successful collection response:

{
  "success": true,
  "data": []
}

Error response:

{
  "success": false,
  "error": "Error description"
}

---

## 5. Health

### GET /api/v1/health

Authentication:

Not required.

Response:

{
  "success": true,
  "service": "N-PuDo-N Backend API",
  "version": "v1",
  "status": "ok",
  "timestamp": "ISO-8601 timestamp"
}

---

# 6. Deliveries API

## GET /api/v1/deliveries

Authentication:

Required.

Returns deliveries available through the backend.

Response:

{
  "success": true,
  "data": [
    {
      "trackingId": "TRK-12345",
      "sender": "Sender",
      "customer": "Customer",
      "hubName": "Hub A",
      "status": "PENDING",
      "weight": "2kg",
      "timestamp": "2026-10-04T12:00:00.000Z"
    }
  ]
}

---

## POST /api/v1/deliveries

Authentication:

Required.

Phase 1 compatibility endpoint.

Request:

{
  "trackingId": "TRK-12345",
  "sender": "Sender",
  "customer": "Customer",
  "hubName": "Hub A",
  "status": "PENDING",
  "weight": "2kg",
  "timestamp": "2026-10-04T12:00:00.000Z"
}

Required fields:

- sender
- customer

The backend may generate:

- trackingId
- timestamp

if they are not supplied.

Important:

This compatibility endpoint does NOT define the final N-PuDo-N business state machine.

---

## DELETE /api/v1/deliveries/:trackingId

Authentication:

Required.

Response:

{
  "success": true,
  "trackingId": "TRK-12345"
}

Important:

This endpoint is a Phase 1 compatibility operation.

It must not be interpreted by clients as:

- parcel cancellation
- parcel return
- hub handover
- settlement

The canonical business state machine will own those operations in a later phase.

---

# 7. Hubs API

## GET /api/v1/hubs

Authentication:

Required.

Response:

{
  "success": true,
  "data": []
}

Hub selection must remain controlled by backend business rules.

Client-side selection must not bypass:

- authentication
- hub status
- hub capacity
- eligibility rules

---

# 8. Canonical N-PuDo-N Business Invariants

The following rules are authoritative.

## 8.1 Registration is not handover

Registration != Handover != Settlement

A parcel being registered for a hub does not mean that custody has transferred.

---

## 8.2 Registration eligibility

Hub registration may occur only when:

- CUSTOMER_REQUEST
- FAILED_HOME_DELIVERY

are the source conditions.

End-of-shift bulk registration is an exception/fallback mechanism, not the primary business path.

---

## 8.3 Hub selection

The suggested hub must consider:

- proximity
- hub status
- available capacity

Direct distance alone is not sufficient.

---

## 8.4 Offline assignment

A courier may assign a parcel to a hub while offline.

The assignment must be synchronized later.

Final business confirmation remains backend-controlled.

---

## 8.5 Custody transfer

Courier -> Hub requires a valid handover/transfer confirmation code.

Hub -> Customer occurs only during final customer collection.

---

## 8.6 Settlement

Settlement is allowed only after valid transfer/acceptance.

Pending settlement states include:

- HUB_SELECTED
- AWAITING_HUB_CONFIRMATION
- HANDOVER_IN_PROGRESS

Settlement must never be triggered merely by local registration.

---

# 9. Canonical Delivery State Machine

The canonical states are:

1. CREATED
2. DELIVERY_ATTEMPTED
3. DELIVERY_FAILED
4. ELIGIBLE_FOR_HUB
5. HUB_SELECTED
6. AWAITING_HUB_CONFIRMATION
7. TRANSFERRED_TO_HUB
8. READY_FOR_CUSTOMER
9. COLLECTED
10. EXPIRED
11. RETURNED_TO_CARRIER
12. CANCELLED
13. TRANSFER_REJECTED

The state machine contains 24 valid transitions.

Clients must not invent additional business states.

Legacy client values such as:

- PENDING
- IN_HUB
- DELIVERED
- FLAGGED

are not canonical states.

They must be explicitly mapped during client migration.

State transitions must be performed by the backend.

Clients must not directly modify delivery status in Supabase.

---

# 10. Idempotency and Audit

Business operations must be idempotent.

Registration transactions must use a transaction identifier.

Audit records must preserve:

- actorId
- actorRole
- oldValue
- newValue
- transactionId
- timestamp

Retrying the same operation must not create duplicate business events.

---

# 11. Offline-First Architecture

Android:

UI
 ->
Local Repository
 ->
Room / Offline Queue
 ->
Backend API v1
 ->
Supabase

PWA:

UI
 ->
Local Repository
 ->
IndexedDB / Offline Queue
 ->
Backend API v1
 ->
Supabase

Local storage is an offline synchronization mechanism.

It is not a second source of business truth.

After migration:

Android and PWA must not perform direct business CRUD against Supabase.

---

# 12. Pricing Model A

Pricing is calculated after registration.

Reference shipping/postage amount:

The postage amount printed on the parcel.

Rules:

- delivery under 12 hours: 20%
- delivery up to 24 hours: 40%
- each additional 24 hours: +50%
- after one week the delivery cycle ends unless sender/recipient pays the delivery fee

Base service fees:

- SMALL: 18,000 T
- MEDIUM: 25,000 T
- LARGE: 35,000 T

Example size multipliers:

- SMALL: 1.0
- MEDIUM: 1.2
- LARGE: 1.5
- OVERSIZE: 2.0

Tariffs must be versioned and snapshotted.

Clients must not independently implement a second pricing engine.

---

# 13. Phase 2 Client Migration Rule

P2-2 will migrate Android and PWA transport from direct Supabase REST access to Backend API v1.

P2-2 must:

- use the existing backend
- use the existing Supabase project
- use the existing Render service
- send Supabase access tokens as Bearer JWT
- preserve offline-first behavior
- preserve existing business invariants
- preserve existing local caches
- use the canonical API paths defined here

P2-2 must NOT:

- create another API
- create another database
- expose the Supabase Service Role Key
- bypass backend authentication
- duplicate the state machine
- change the canonical state machine
- implement route-backtracking intelligence
- replace Supabase
- replace Render
- modify Supabase schema without explicit authorization

---

# 14. Phase 1 Completion Criteria

P2-1 is complete when:

1. This document exists at:

docs/architecture/API-CONTRACT-V1.md

2. Backend API v1 paths are locked.

3. Authentication contract is locked.

4. Response envelope is locked.

5. Canonical state machine is documented.

6. Offline-first boundary is documented.

7. Pricing rules are documented.

8. P2-2 uses this document as its source of truth.

---

## Status

P2-1 API Contract Lock

Status: LOCKED

Next phase:

P2-2 Client Transport Migration
