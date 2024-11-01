# Install dependencies only when needed
FROM node:20-alpine AS base
WORKDIR /todo-ui

# Copy package files and install dependencies
COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile

# Rebuild the source code only when needed
FROM base AS builder
WORKDIR /todo-ui

# Copy all source files and node_modules from the base stage
COPY . .
COPY --from=base /todo-ui/node_modules ./node_modules
RUN yarn build

# Production image, copy necessary files
FROM base AS runner
WORKDIR /todo-ui

ENV NODE_ENV production

# Add user and group for secure container execution
RUN addgroup -g 1001 -S nodejs
RUN adduser -S nextjs -u 1001

# Copy built files and configuration from the builder stage
COPY --from=builder /todo-ui/next.config.ts ./
COPY --from=builder --chown=nextjs:nodejs /todo-ui/.next ./.next
COPY --from=builder /todo-ui/node_modules ./node_modules
COPY --from=builder /todo-ui/package.json ./package.json

# Switch to non-root user
USER nextjs

# Expose application port
EXPOSE 3000

# Run application
CMD ["yarn", "start"]