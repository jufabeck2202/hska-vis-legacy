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



json=$(curl -X POST --location "http://webshop:secret@localhost:8001/oauth/token" -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=password&username=admin&password=admin&scope=write")
token=$( jq -r ".access_token" <<<"$json" )
echo ""
echo $token
echo

curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Cache-Control: no-cache"\
    -H "Authorization: Bearer $token"
curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Cache-Control: no-cache" \
    -H "Authorization: Bearer $token" 
curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Authorization: Bearer $token" \
    -H "Cache-Control: no-cache"
curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Authorization: Bearer $token" \
    -H "Cache-Control: no-cache"

curl -X POST --location "http://localhost:8003/categories" \
    -H "Authorization: Bearer $token" \
    -H "content-type: application/json" \
    -d "{
        \"name\": \"Kicker-Ersatzteile\",
        \"productIds\": \"\"
        }"

curl -X POST --location "http://localhost:8003/categories" \
    -H "Authorization: Bearer $token" \
    -H "content-type: application/json" \
    -d "{
        \"name\": \"Bausätze\",
        \"productIds\": \"\"
        }"

curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Authorization: Bearer $token" \
    -H "Cache-Control: no-cache"

curl -X GET --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Authorization: Bearer $token" \
    -H "Cache-Control: no-cache"

curl -X POST --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Authorization: Bearer $token" \
    -H "content-type: application/json" \
    -d "{
        \"name\": \"Figur\",
        \"price\": 10.0,
        \"categoryId\": 1,
        \"details\": \"Kickerfigur für zerbrochene Spieler\"
        }"

curl -X POST --location "http://localhost:8081/productscomposite-service/products/" \
    -H "Authorization: Bearer $token" \
    -H "content-type: application/json" \
    -d "{
        \"name\": \"SuperkickerX\",
        \"price\": 5000.0,
        \"categoryId\": 2,
        \"details\": \"Bausatz zum selberbauen\"
        }"

