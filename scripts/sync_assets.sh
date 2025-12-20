#!/usr/bin/env bash
set -euo pipefail

# Sync assets from possible source locations into the Android app module.
# Looks for assets in: ./assets, ../assets, ./images, ../images, etc.

REPO_ROOT=$(cd "$(dirname "$0")/.." && pwd)

declare -A SRC_MAP=(
  [assets]="${REPO_ROOT}/assets ${REPO_ROOT}/../assets"
  [images]="${REPO_ROOT}/images ${REPO_ROOT}/../images"
  [fonts]="${REPO_ROOT}/fonts ${REPO_ROOT}/../fonts"
  [sounds]="${REPO_ROOT}/sounds ${REPO_ROOT}/../sounds"
  [libs]="${REPO_ROOT}/libs ${REPO_ROOT}/../libs"
)

DEST_ASSETS="${REPO_ROOT}/app/src/main/assets"
DEST_DRAWABLE="${REPO_ROOT}/app/src/main/res/drawable"
DEST_FONT="${REPO_ROOT}/app/src/main/res/font"
DEST_RAW="${REPO_ROOT}/app/src/main/res/raw"
DEST_LIBS="${REPO_ROOT}/app/libs"

mkdir -p "$DEST_ASSETS" "$DEST_DRAWABLE" "$DEST_FONT" "$DEST_RAW" "$DEST_LIBS"

copy_if_exists() {
  local src_dirs="$1" dest="$2" pattern="${3:-*}"
  for d in $src_dirs; do
    if [ -d "$d" ]; then
      echo "Syncing from $d -> $dest"
      # copy recursively, preserve names; skip hidden files
      shopt -s dotglob
      for f in "$d"/$pattern; do
        [ -e "$f" ] || continue
        cp -R "$f" "$dest/"
      done
      shopt -u dotglob
    fi
  done
}

# assets -> app/src/main/assets
copy_if_exists "${SRC_MAP[assets]}" "$DEST_ASSETS"

# images -> drawable
copy_if_exists "${SRC_MAP[images]}" "$DEST_DRAWABLE" "*.png"
copy_if_exists "${SRC_MAP[images]}" "$DEST_DRAWABLE" "*.jpg"
copy_if_exists "${SRC_MAP[images]}" "$DEST_DRAWABLE" "*.webp"

# fonts -> res/font
copy_if_exists "${SRC_MAP[fonts]}" "$DEST_FONT" "*.ttf"
copy_if_exists "${SRC_MAP[fonts]}" "$DEST_FONT" "*.otf"

# sounds -> res/raw
copy_if_exists "${SRC_MAP[sounds]}" "$DEST_RAW" "*.mp3"
copy_if_exists "${SRC_MAP[sounds]}" "$DEST_RAW" "*.wav"

# libs -> app/libs
copy_if_exists "${SRC_MAP[libs]}" "$DEST_LIBS"

echo "Sync complete."
