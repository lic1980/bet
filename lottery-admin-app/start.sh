#!/bin/sh
sed -i  "s|<body>|<body backend-host=\"$BACKEND_HOST\">|"  /usr/share/nginx/html/index.html
nginx -g "daemon off;"