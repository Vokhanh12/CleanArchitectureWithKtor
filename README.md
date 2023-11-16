# CleanArchitectureWithKtor

<h2>Cấu trúc mô-đun (Module structure)</h2>


![image](https://github.com/Vokhanh12/CleanArchitectureWithKtor/assets/36543564/1cfdd173-980c-488c-aba3-1f6ebffbe40a)




<h3>1.Tiêm phụ thuộc(Dependency Injection):</h3>


Các thể hiện của OrderService và OrderRepository của bạn được tạo ra ngoài logic định tuyến.
Nếu dự án của bạn phát triển, việc sử dụng dependency injection để quản lý những thể hiện này là một thực hành tốt.


<h3>2.Kết nối Cơ sở Dữ liệu(Database Connection):</h3>


Hiện tại, dự án của bạn có vẻ sử dụng một kho lưu trữ trong bộ nhớ (OrderRepository) để lưu trữ dữ liệu.
Nếu bạn muốn kết nối với một cơ sở dữ liệu thực tế (như SQL Server), bạn cần cập nhật kho lưu trữ của mình để tương tác với cơ sở dữ liệu.


<h3>3.Logic Định Tuyến và Bộ Điều Khiển(Routing and Controllers):</h3>


Logic định tuyến của bạn (addOrderRoutes) được tổ chức tốt và phân tách các trách nhiệm.
Tuy nhiên, khi dự án của bạn mở rộng, xem xét tổ chức các route của bạn vào các bộ điều khiển để cải thiện khả năng bảo trì.


<h3>4.Đối tượng truyền dữ liệu(DTOs - Data Transfer Objects):</h3>


DTOs của bạn có tên phù hợp (CreateOrderDto, UpdateOrderDto, OrderDto).
Đảm bảo rằng các loại dữ liệu trong các DTO này khớp với các loại dữ liệu tương ứng trong thực thể của bạn.


<h3>5.Tuần tự hóa(Serialization):</h3>


Rất tốt khi bạn sử dụng content negotiation và serialization của Ktor. 
Điều này quan trọng để xử lý dữ liệu JSON.


<h3>6.Cấu Hình Kết Nối Cơ sở Dữ liệu(Database Connection Configuration):</h3>


Trong hàm configureDatabases của bạn, bạn đang kết nối đến một cơ sở dữ liệu SQL Server. 
Đảm bảo rằng chi tiết kết nối là chính xác và rằng instance SQL Server của bạn có thể truy cập được.


<h3>7.Xử Lý Lỗi(Error Handling):</h3>


Xem xét việc thêm xử lý lỗi mạnh mẽ hơn trong logic định tuyến của bạn để cung cấp các phản hồi có ý nghĩa cho các tình huống khác nhau (ví dụ: yêu cầu không hợp lệ, lỗi cơ sở dữ liệu).


<h3>8.Kiểm Thử(Testing):</h3>


Xem xét việc viết các bài kiểm thử đơn vị, đặc biệt là đối với các thành phần quan trọng như các dịch vụ và kho lưu trữ.


<h2>Cập nhật cơ sở dữ liệu với mô hinh</h2>




Để cập nhật kho lưu trữ của bạn để tương tác với cơ sở dữ liệu SQL Server, bạn cần thực hiện một số bước. Dưới đây là hướng dẫn giả định với sử dụng thư viện Exposed để kết nối và thao tác với SQL Server.

<h3>Bước 1: Thêm Dependency</h3>


Thêm dependency của Exposed vào file build.gradle.kts hoặc build.gradle của bạn:

```gradle
val exposed_version: String by project

implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
```

<h3>Bước 2: Cấu hình Kết Nối</h
                              
                              3>
Cấu hình kết nối đến cơ sở dữ liệu SQL Server trong hàm configureDatabases của bạn. Thay thế phần cấu hình hiện tại bằng thông tin kết nối của bạn:


```gradle
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    Database.connect(
        url = "jdbc:sqlserver://<your-server>:<port>;databaseName=<your-database>",
        driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        user = "<your-username>",
        password = "<your-password>"
    )
}
```


<h3>Bước 3: Cập Nhật Kho Lưu Trữ</h3>


Cập nhật kho lưu trữ của bạn để sử dụng Exposed thay vì lưu trữ trong bộ nhớ. Dưới đây là một ví dụ với OrderRepository:


```kotlin
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*

object Orders : IntIdTable() {
    val name = varchar("name", 255)
}

class OrderRepository : BaseRepository<Order>() , IOrderRepository {

    override suspend fun getAll(): List<Order> {
        return transaction {
            Orders.selectAll().map { rowToOrder(it) }
        }
    }

    override suspend fun findById(id: String): Order? {
        return transaction {
            Orders.select { Orders.id eq id.toInt() }.singleOrNull()?.let { rowToOrder(it) }
        }
    }

    override suspend fun insert(entity: Order) {
        transaction {
            val id = Orders
                .insertAndGetId {
                    it[name] = entity.name
                }
            entity.id = id.value.toString()
        }
    }

    override suspend fun delete(id: String) {
        transaction {
            Orders.deleteWhere { Orders.id eq id.toInt() }
        }
    }

    override suspend fun replace(item: Order) {
        transaction {
            Orders.update({ Orders.id eq item.id.toInt() }) {
                it[name] = item.name
            }
        }
    }

    private fun rowToOrder(row: ResultRow): Order {
        return Order(row[Orders.id].value.toString()).apply {
            name = row[Orders.name]
        }
    }
}
```

