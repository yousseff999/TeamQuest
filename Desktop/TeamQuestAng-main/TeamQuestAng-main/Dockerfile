# Build stage
FROM node:20 AS build

WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --configuration production

# Run stage
FROM nginx:alpine
COPY --from=build /app/dist/team-quest-ang /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
