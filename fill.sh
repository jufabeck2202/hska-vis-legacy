curl -X POST --location "http://localhost:8001/users/" \
    -H "content-type: application/json" \
    -d "{
          \"username\": \"admin\",
          \"firstname\": \"super\",
          \"lastname\": \"admin\",
          \"password\": \"admin\",
          \"roletype\": \"admin\",
          \"rolelevel\": 0
        }"
        
curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Cache-Control: no-cache"
curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Cache-Control: no-cache"
curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Cache-Control: no-cache"
curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Cache-Control: no-cache"

curl -X POST --location "http://localhost:8003/categories" \
    -H "content-type: application/json" \
    -d "{
        \"name\": \"Kicker-Ersatzteile\",
        \"productIds\": \"\"
        }"

curl -X POST --location "http://localhost:8003/categories" \
    -H "content-type: application/json" \
    -d "{
        \"name\": \"Bausätze\",
        \"productIds\": \"\"
        }"

curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Cache-Control: no-cache"

curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Cache-Control: no-cache"

curl -X POST --location "http://localhost:8081/productscomposite-service/products/" \
    -H "content-type: application/json" \
    -d "{
        \"name\": \"Figur\",
        \"price\": 10.0,
        \"categoryId\": 1,
        \"details\": \"Kickerfigur für zerbrochene Spieler\"
        }"

curl -X POST --location "http://localhost:8081/productscomposite-service/products/" \
    -H "content-type: application/json" \
    -d "{
        \"name\": \"SuperkickerX\",
        \"price\": 5000.0,
        \"categoryId\": 2,
        \"details\": \"Bausatz zum selberbauen\"
        }"

