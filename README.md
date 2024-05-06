# springboot-mall
基於SpringBoot、MySQL、Vue 實作商店系統

線上Demo : <https://justinwuproject.online/mall/>

此專案為後端程式碼

前端程式碼請前往 : <https://github.com/justinwu1220/vue3-mall>

## 技術
- JAVA Spring Boot :
  - MVC架構實作後端程式
  - JDBC連接資料庫
    
- MySQL :
  - 建立資料庫
    - 商品
    - 購物車
    - 訂單
    - 用戶
    
- JWT :
  - 使用JWT實作登入驗證
 
- Vue :
  - 使用Vue作為前端框架
  - Vue Router 配置路由

- Pinia
  - 前端狀態管理及持久化

- AWS
  - EC2 雲端部署

- Docker
  - docker-compose 
  - 容器化部署

- Nginx
  - 伺服器反向代理

## 專案說明

* 主要功能
  
  * 商品功能
    * 管理商品CRUD
      
  * 購物車功能
    * 管理購物車CRUD
      
  * 訂單功能
    * 創建、查看訂單
      
  * 用戶功能
    * 註冊、登入
    * 管理用戶購物車
    * 查看用戶訂單
   
* 權限管理
  * ADMIN
    * 商品CRUD請求✅
    * 查看全部訂單請求✅
    * 查看全部用戶請求✅
      
  * CLIENT
    * 查詢商品請求✅
    * 增刪改商品請求⛔
    * 個人購物車商品CRUD請求✅
    * 查詢個人訂單請求✅
    * 查看全部訂單請求⛔
    * 查看全部用戶請求⛔
      
* 專案結構
```lua
src
└─main
    └─java
       └─com.justinwu.springbootmall
          ├─config -- 管理配置
          ├─constant -- 管理常數項
          ├─controller -- 控制層
          ├─dao -- 數據訪問層
          │  └─impl
          ├─dto -- 資料傳輸物件
          ├─exception -- 管理例外
          ├─interceptor -- 攔截器
          ├─model -- 數據模型
          ├─rowmapper -- 管理JDBC rowmapper
          ├─service -- 業務邏輯層
          │  └─impl
          ├─tool -- 非通用工具
          └─util -- 通用工具

```
* 使用MVC架構 :
  * Controller
    - 處理使用者的Http請求，驗證請求參數
    - 組織路由邏輯和協調服務層
  * Service
    - 處裡業務邏輯
    - 接收來自控制器的請求，並基於業務邏輯與DAO進行交互
    - 封裝應用的業務邏輯
  * DAO
    - 與資料庫進行直接的 CRUD 操作
    - 封裝數據訪問細節
  ```lua
  ├─controller
  │      CartController.java
  │      OrderController.java
  │      ProductController.java
  │      UserController.java
  │      
  ├─service 
  │  └─impl
  │     CartServiceImpl.java
  │     OrderServiceImpl.java
  │     ProductServiceImpl.java
  │     UserServiceImpl.java
  │      
  └─dao
    └─impl
       CartDaoImpl.java
       OrderDaoImpl.java
       ProductDaoImpl.java
       UserDaoImpl.java
  ```

* JWT(JSON Web Token)
  * 使用JWT驗證使用者登入及權限
    ```java
    jwtToken = jwtBuilder
                //Header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //Payload
                .setId(user.getUserId().toString())
                .claim("email",user.getEmail())
                .setExpiration( new Date(System.currentTimeMillis() + time)) //token存活時間
                //Signature
                .signWith(SignatureAlgorithm.HS256, sign) //設定加密算法和簽名
                //連接成字串
                .compact();
    ```
    
* Interceptor
  * 搭配JWT管理使用者Http請求，依權限允許訪問
    
    驗證請求用戶是否存在
    ```java
    if (userLoginToken.required()) {
        //驗證
        if (!JwtUtil.checkJwtToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "無token，token過期或不正確，請重新登入");
        }

        // 獲取 token 中的 userId
        Claims claims = JwtUtil.parseJwtToken(token);
        Integer userId = Integer.valueOf(claims.getId());

        //查詢用戶是否存在
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用戶不存在，請重新登入");
        }

        return true;
    }
    ```
    驗證請求權限
    ```java
    if(user.getAuthority().equals(Authority.ADMIN)){
        return true;
    } else{
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用戶權限不足");
    }
    ```

* 資料庫
  * MySQL
 
    商品
    ```sql
    CREATE TABLE `product`  (
      `product_id` int NOT NULL AUTO_INCREMENT,
      `product_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `image_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `price` int NOT NULL,
      `stock` int NOT NULL,
      `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
      `created_date` timestamp NOT NULL,
      `last_modified_date` timestamp NOT NULL,
      PRIMARY KEY (`product_id`) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
    ```
    商品其他圖片
    ```sql
    CREATE TABLE `product_other_images`  (
      `image_id` int NOT NULL AUTO_INCREMENT,
      `product_id` int NOT NULL,
      `image_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      PRIMARY KEY (`image_id`) USING BTREE,
      INDEX `product_id`(`product_id` ASC) USING BTREE,
      CONSTRAINT `product_other_images_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
    ) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
    ```
    使用者
    ```sql
    CREATE TABLE `user`  (
      `user_id` int NOT NULL AUTO_INCREMENT,
      `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `authority` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `created_date` timestamp NOT NULL,
      `last_modified_date` timestamp NOT NULL,
      PRIMARY KEY (`user_id`) USING BTREE,
      UNIQUE INDEX `email`(`email` ASC) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
    ```
    使用者收貨資訊
    ```sql
    CREATE TABLE `user_address_info`  (
      `user_address_info_id` int NOT NULL AUTO_INCREMENT,
      `user_id` int NOT NULL,
      `receiver` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `contact` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `address` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      PRIMARY KEY (`user_address_info_id`) USING BTREE,
      INDEX `user_id`(`user_id` ASC) USING BTREE,
      CONSTRAINT `user_address_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
    ) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
    ```
    購物車商品
    ```sql
    CREATE TABLE `cart_item`  (
      `cart_item_id` int NOT NULL AUTO_INCREMENT,
      `user_id` int NOT NULL,
      `product_id` int NOT NULL,
      `quantity` int NOT NULL,
      `selected` tinyint(1) NOT NULL,
      PRIMARY KEY (`cart_item_id`) USING BTREE,
      INDEX `product_id`(`product_id` ASC) USING BTREE,
      INDEX `user_id`(`user_id` ASC) USING BTREE,
      CONSTRAINT `cart_item_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
      CONSTRAINT `cart_item_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
    ) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
    ```
    訂單
    ```sql
    CREATE TABLE `order`  (
      `order_id` int NOT NULL AUTO_INCREMENT,
      `user_id` int NOT NULL,
      `total_amount` int NOT NULL,
      `state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `receiver` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `contact` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `address` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
      `created_date` timestamp NOT NULL,
      `last_modified_date` timestamp NOT NULL,
      PRIMARY KEY (`order_id`) USING BTREE,
      INDEX `user_id`(`user_id` ASC) USING BTREE,
      CONSTRAINT `order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
    ) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
    ```
    訂單商品
    ```sql
    CREATE TABLE `order_item`  (
      `order_item_id` int NOT NULL AUTO_INCREMENT,
      `order_id` int NOT NULL,
      `product_id` int NOT NULL,
      `quantity` int NOT NULL,
      `amount` int NOT NULL,
      PRIMARY KEY (`order_item_id`) USING BTREE,
      INDEX `order_id`(`order_id` ASC) USING BTREE,
      INDEX `product_id`(`product_id` ASC) USING BTREE,
      CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
      CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
    ) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
    ```
    <br/>
      
## 頁面預覽

* 商店頁面

  <img src="https://github.com/justinwu1220/springboot-mall/blob/main/img/1.png" >
  <br/><br/>
  
* 後台管理頁面

  <img src="https://github.com/justinwu1220/springboot-mall/blob/main/img/2.png" >
  <br/><br/>

* 登入頁面
  
  <img src="https://github.com/justinwu1220/springboot-mall/blob/main/img/4.png" >
  <br/><br/>

## 功能(Gif展示)

* 分頁展示商品及依類別展示商品

  ![gif](https://github.com/justinwu1220/springboot-mall/blob/main/img/1.gif)
  <br/><br/>

* 搜尋商品

  ![gif](https://github.com/justinwu1220/springboot-mall/blob/main/img/2.gif)
  <br/><br/>

* 查看商品詳情

  ![gif](https://github.com/justinwu1220/springboot-mall/blob/main/img/3.gif)
  <br/><br/>

* 商品加入購物車及修改購物車內容

  ![gif](https://github.com/justinwu1220/springboot-mall/blob/main/img/4.gif)
  <br/><br/>
  
* 填寫收貨資訊及送出訂單

  ![gif](https://github.com/justinwu1220/springboot-mall/blob/main/img/5.gif)
  <br/><br/>

* 管理頁面商品增刪查改

  ![gif](https://github.com/justinwu1220/springboot-mall/blob/main/img/6.gif)
  <br/><br/>

* 管理頁面訂單及用戶查看

  ![gif](https://github.com/justinwu1220/springboot-mall/blob/main/img/7.gif)
  <br/><br/>

* 註冊及登入

  ![gif](https://github.com/justinwu1220/springboot-mall/blob/main/img/8.gif)
  <br/><br/>

## 雲端部署

* AWS
  * EC2 t2.mirco
  * OS: Ubuntu Server 22.04 LTS

* Docker
  * docker-compose部署
    ```docker
    version: '3.8'
    services:
      frontend:
        image: justinwu12/vue3-mall
        container_name: mall-frontend
        ports:
          - "80:80"
      backend:
        image: justinwu12/springboot-mall
        user: root
        container_name: mall-backend
        ports:
          - "8080:8080"
        depends_on:
          - db
        environment:
          - SPRING_DATASOURCE_URL=${SPRING_MALL_DATASOURCE_URL}
          - SPRING_DATASOURCE_USERNAME=${SPRING_MALL_DATASOURCE_USERNAME}
          - SPRING_DATASOURCE_PASSWORD=${SPRING_MALL_DATASOURCE_PASSWORD}
      db:
        image: mysql
        container_name: mysql
        ports:
          - "3306:3306"
        environment:
          - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
        volumes:
          - db-data:/var/lib/mysql
          - /初始資料庫路徑
      nginx:
        image: nginx
        container_name: nginx-proxy
        volumes:
          - /nginx.conf路徑
          - /SSL證書路徑
        ports:
          - "443:443"
        depends_on:
          - frontend
    volumes:
      db-data:
    ```
* Nginx
  * SSL證書管理
  * 伺服器反向代理
