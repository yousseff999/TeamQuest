# Stage 1: Build Angular app
FROM node:18 as build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build -- --configuration=production

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/team-quest-ang /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
