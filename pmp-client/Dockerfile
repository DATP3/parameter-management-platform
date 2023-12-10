# FROM node:alpine
FROM node:alpine3.17
EXPOSE 5173
RUN mkdir /opt/app
COPY . /opt/app/
WORKDIR /opt/app
RUN npm install
RUN npm run build
CMD [ "npm", "run", "preview", "--", "--host" ]
