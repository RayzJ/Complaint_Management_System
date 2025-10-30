FROM nginx:alpine

WORKDIR /usr/share/nginx/html

# Copy pre-built files (we'll build locally)
COPY dist/complaint-management-frontend/ .
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80