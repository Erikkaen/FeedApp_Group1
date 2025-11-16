# Stage 1: Build the Svelte app
FROM node:20-alpine AS builder

WORKDIR /app
RUN apk add --no-cache bash build-base

# Install dependencies
COPY package*.json ./
RUN npm ci --frozen-lockfile

# Copy app files
COPY . .

# Build the app
RUN npm run build

# Stage 2: Serve static assets with Nginx
FROM nginx:alpine

# Copy the built assets from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
