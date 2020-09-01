# Installation:

```
npm install
npm update

gradle clean shadowJar
```

# Running:

```
java -jar build/libs/TodoMVC.jar
```

Java program will host on port 4567 (sparkjava default)

Forwarding from port 80 to that port varies depending on implementation. For this project I just added the following to /etc/nginx/sites-enabled/default

```
        location /api {
                proxy_set_header   X-Forwarded-For $remote_addr;
                proxy_set_header   Host $http_host;
                proxy_pass         "http://127.0.0.1:4567";
        }
```

I have nginx hosting the angular app and the api exposed via the above.
