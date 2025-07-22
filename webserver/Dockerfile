FROM nginx:mainline-alpine-slim

COPY nginx.conf /etc/nginx/nginx.conf
COPY ./templates /etc/nginx/templates

EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]