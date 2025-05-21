# Base stage with pnpm setup
FROM node:22.15-alpine AS base

# Production dependencies stage
FROM base AS deps
COPY package.json package-lock.json ./
RUN npm ci

# Build stage - install all dependencies and build
FROM base AS build
COPY package.json package-lock.json ./
# Copy installed dependencies
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN npm run build

# Final stage - combine production dependencies and build output
FROM base AS runner
WORKDIR /app
COPY --from=deps --chown=node:node /app/node_modules ./node_modules
COPY --from=build --chown=node:node /app/dist ./dist

# Use the node user from the image
USER node

# Expose port 8080
EXPOSE 8080

# Start the server
CMD ["node", "dist/app.js"]