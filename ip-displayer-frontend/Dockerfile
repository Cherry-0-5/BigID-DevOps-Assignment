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
COPY --from=build --chown=nonroot:nonroot /app /app

# Configuring Environment variables
ENV NODE_ENV=production \
    PORT=8080 \
    BACKEND_URL=http://ip-echo-api-service:8088

# Using non-root user for Distroless image
USER nonroot

# Exposing frontend to default 8080 port
EXPOSE 8080

# To start frontend application
CMD ["/nodejs/bin/node", "server.js"]
