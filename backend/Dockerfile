FROM node:20-alpine

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied where available
COPY package*.json ./

RUN npm ci --only=production

# Bundle app source
COPY . .

# Render dynamically assigns PORT via environment variable
ENV PORT=3000
EXPOSE 3000

# Start the application
CMD [ "node", "index.js" ]
