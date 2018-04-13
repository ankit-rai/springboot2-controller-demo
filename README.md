# springboot2-controller-demo
A demo app for spring boot 2 using Functional Endpoints.

To run the app, use following command (you need Java 8 for this)
```
./gradlew clean build bootRun
```

To test the app, you can use following URLs

**Success cases**
```
curl -v -H "Authorization: abc134" -H "From: 1234"  http://localhost:5002/users/U1
curl -v -H "Authorization: ott-2345jjKK" -H "From: 1234"  http://localhost:5002/users/U1
curl -v -H "Authorization: ott-2345jjKK" -H "From: 1234" -H "X-Trace-Id: xab-1234-9987" http://localhost:5002/users/U1
```

**Failure cases**
```
curl -v -H "Authorization: xyz98766" -H "From: 1234"  http://localhost:5002/users/U1
curl -v -H "Authorization: xott-098112" -H "From: 1234"  http://localhost:5002/users/U1
curl -v -H "Authorization: abc-098112" -H "From: 1234"  http://localhost:5002/users/U100
curl -v -H "Authorization: abc-098112" -H "From: 1234" -H "X-Trace-Id: f987-134ad" http://localhost:5002/users/U100
curl -v -H "Authorization: abc-098112" -H "From: 1234"  http://localhost:5002/users/U-1
curl -v -H "Authorization: ott-098112" -H "From: 1234" -H "X-Trace-Id: afd-3465-a123"  http://localhost:5002/users/U-1
```
