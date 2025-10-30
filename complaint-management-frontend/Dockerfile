# ----------- Stage 1: Build the Angular app -----------
FROM node:20-alpine AS build

# Set the working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy all project files and build the app
COPY . .
RUN npm run build --prod

# ----------- Stage 2: Serve with Nginx -----------
FROM nginx:alpine

# Copy built Angular app from build stage
COPY --from=build /app/dist/* /usr/share/nginx/html

# Copy custom nginx configuration (optional)
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]