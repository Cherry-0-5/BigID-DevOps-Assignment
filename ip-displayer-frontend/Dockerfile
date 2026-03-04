# --- Stage 1: Build ---
FROM node:20-alpine AS build
# Setting WORKDIR early
WORKDIR /app

COPY package*.json ./
RUN npm install --production

COPY . .

# --- Stage 2: Runner ---
FROM gcr.io/distroless/nodejs20-debian12
# CRITICAL: Re-declare WORKDIR so the image knows where it is
WORKDIR /app

# Copy everything from the build stage
COPY --from=build --chown=nonroot:nonroot /app /app

# Using the absolute path to the binary AND the script
# This prevents the "ELF" syntax error by being explicit
ENTRYPOINT ["/nodejs/bin/node"]
CMD ["/app/server.js"]

USER nonroot
EXPOSE 8080
