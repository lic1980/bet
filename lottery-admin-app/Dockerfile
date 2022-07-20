FROM nginx:1-alpine
COPY ./nginx/prod.conf /etc/nginx/conf.d/default.conf   
COPY ./dist/ /usr/share/nginx/html/
COPY ./start.sh /app/

ENTRYPOINT ["/app/start.sh"]