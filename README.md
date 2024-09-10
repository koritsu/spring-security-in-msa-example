# 로컬 redis 설치
docker run --name redis -d -p 6379:6379 redis:latest

# 토큰 발급
curl -X POST 'http://localhost:8080/auth/login' --header 'Content-Type: application/json' --data '{"username":"user","password":"password"}'

# USER 권한만 사용 가능 테스트
curl -X GET 'http://localhost:8080/products/has-user-role' --header 'Authorization: Bearer {token}'

# ADMIN 권한만 사용 가능 테스트
curl -X GET 'http://localhost:8080/products/has-admin-role' --header 'Authorization: Bearer {token}'

# 토큰 없을 때, 사용 가능 테스트
curl -X GET 'http://localhost:8080/products/has-no-role' --header 'Authorization: Bearer {token}'

