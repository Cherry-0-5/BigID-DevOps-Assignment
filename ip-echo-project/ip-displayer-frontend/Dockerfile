# Build stage
FROM node:20-alpine AS build
WORKDIR /app

# Install dependencies - to leverage Docker cache
COPY package*.json ./
RUN npm install --production

# Copy the rest of the application code
COPY . .

# Runner stage - Distroless image
FROM gcr.io/distroless/nodejs20-debian12
WORKDIR /app

# Copy from build stage with given appuser permission to copy app related files
COPY --from=build --chown=1000:1000 /app /app

# Configuring Environment variables
ENV NODE_ENV=production \
    PORT=80 \
    BACKEND_URL=http://ip-echo-service:8088

# UID of App user for Distroless image
USER 1000

# Exposing frontend to default 80 port
EXPOSE 80

# To start frontend application
CMD ["/nodejs/bin/node", "server.js"]
