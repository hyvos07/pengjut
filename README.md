# ⚙️ Advanced Programming - Tutorial

### 🛍️ AdvProg Eshop

**Nama**: Daniel Liman <br>
**NPM**: 2306220753 <br>
**Kelas**: A

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hyvos07_pengjut&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=hyvos07_pengjut)

## Modul 3

### Reflection
> Explain what principles you apply to your project!
Saya telah mengimplementasi 5 prinsip dari SOLID Principle yang terdiri dari:
- **S - Single Responsibility Principle**

    Pada proyek ini, saya mengimplementasi prinsip Single Responsibility Principle yang mengutamakan 1 fungsi untuk setiap class/file yang ada di dalam proyek ini. Contohnya pada class `CarController`, saya memisahkannya dari file `ProductController.java` agar tidak menambah peran dari file tersebut dan menjaga prinsip 1 fungsi untuk setiap class/file. 
    
    Selain itu, saya juga mengimplementasi automasi pemberian ID (yang berupa UUID) langsung pada inisialisasi model `Product` dan `Car` karena pembuatan ID seharusnya dilakukan di saat pembuatan item pada pertama kali.

- **O: Open-Closed Principle**

    Pada proyek ini, saya mencoba untuk mengekstrak keluar logika validasi untuk fungsi `findById()` yang dimiliki oleh `ProductService` agar dapat memodifikasi logika validasi dari `Product` yang saya punya untuk nanti tanpa harus mengubah `ProductService` itu sendiri. Hal ini menunjukkan bahwa ada penerapan untuk prinsip yang membatasi modifikasi kode tetapi memperbolehkan ekstensi dari bagian fungsi itu sendiri.

- **L: Liskov Substitution Principle**

    Pada proyek ini, saya mencoba untuk menghapus penggunaan `extends` yang saya dapat pada tutorial pada `CarController` terhadap `ProductController`, karena aksi tersebut tidak diperlukan dan `CarController` sudah bisa berfungsi tanpa harus meng-*extend* class lainnya. Hal ini menunjukkan penerapan prinsip untuk pengunaan superclass dan subclass yang tepat pada kode yang dibuat.

- **I: Interface Segregation Principle**

    Pada proyek ini, saya menerapkan penggunaan `interface` untuk menggeneralisasi jenis service yang saya pakai pada proyek ini. Maka dari itu, saya membuat interface `BaseService<T>` untuk diimplementasi oleh `CarService` dan `ProductService` nantinya.

- **D: Dependency Inversion Principle**

    Pada proyek ini, saya menggunakan class service langsung dengan implementasinya tanpa memisahkan implementasi di suatu class yang lain. Hal ini saya lakukan untuk bisa menjaga prinsip Dependency Inversion Principle (DIP) agar bisa langsung meng-*implement* interface dari `BaseService<T>` secara langsung tanpa perantara interface lagi.

> Explain the advantages of applying SOLID principles to your project with examples.
Penerapan prinsip SOLID pada proyek saya berdampak dari readibility yang proyek saya punya sekarang. Contohnya, pada implementasi prinsip Interface Segregation Principle (ISP), interface `BaseService<T>` saya dapat menjadi acuan developer lain untuk membuat service baru yang akan dipakai nantinya, bahkan untuk saya sendiri. Hal ini akan memudahkan maintainability dari proyek dan kode yang sudah dibuat.

> Explain the disadvantages of not applying SOLID principles to your project with examples.
Saat tidak adanya penerapan SOLID yang benar, beberapa bagian kode bisa saja menjadi tidak logical dan sulit untuk dimengerti, misalnya pada saat `CarController` masih meng-*extend* class controller lainnya, yaitu `ProductController`. `extends` yang sia-sia tersebut akan menimbulkan kebingungan bahkan kesalahpahaman oleh developer yang melanjutkan proyek ini nantinya. Bisa saja developer lain akan berpikir jika sebuah controller yang dibuat pada proyek ini diharuskan untuk meng-*extend* class controller lainnya, yaitu `ProductController`.

<br>

## Modul 2

### Reflection

> List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

Setelah melakukan analisis kode pada Sonarcloud, saya menemukan beberapa *issues* terkait *clean code*. Salah satu yang saya berhasil perbaiki adalah bagian kode yang mengulang-ngulang, sehingga melanggar prinsip DRY (*Don't Repeat Yourself*). Misalkan pada bagian kode yang ada di [`ProductController.java`](https://github.com/hyvos07/pengjut/blob/decfd5ee9e0753f56ca54314957c90d51dbb53ed/src/main/java/id/ac/ui/cs/advprog/eshop/controller/ProductController.java) berikut.

```java
// ProductController.java
@PutMapping("/edit/{id}")
public String updateProductPut(@ModelAttribute Product product, @PathVariable String id, RedirectAttributes ra) {
    product.setProductId(id);
    try {
        if (product.getProductQuantity() < 0) {
            ra.addFlashAttribute("error", "Alert: Product quantity must be greater than 0!");
            return "redirect:/product/edit/" + id;
        }

        if (product.getProductName().isEmpty()) {
            ra.addFlashAttribute("error", "Alert: Product name cannot be empty!");
            return "redirect:/product/edit/" + id;
        }

        if (!service.update(product)) {
            ra.addFlashAttribute("error", "Alert: Product cannot be updated!");
        } else {
            ra.addFlashAttribute("success", "Alert: Product updated!");
        }
    } catch (Exception e) {
        ra.addFlashAttribute("error", "Alert: Product cannot be updated!");
    }

    return "redirect:/product/list";
}
```
Pada bagian kode di atas, dapat dilihat bahwa *string* `"error"` digunakan berkali-kali di satu *method* yang sama. Bahkan, jika dilihat dalam satu file yang utuh, string `"error"` dan `"success"` dipakai berkali-kali di *controller* ini. Hal ini melanggar prinsip DRY karena membuat ***Maintainability*** dari kode kita berkurang.

Jika suatu saat kita mau mengubah string `"error"` menjadi `"errorMessage"`, kita perlu mengubah satu-satu kemunculan string tersebut di dalam satu file tersebut (Walaupun beberapa IDE memungkinkan kita mengganti value yang sama dalam satu waktu, tentu saja tetap merepotkan jika ada nama yang sama namun bukan bagian dari kode yang akan kita ganti). Jika salah satu komponen yang diubah tidak tersimpan, dapat terjadi kemungkinan error yang tidak diinginkan.

Solusi untuk masalah ini adalah dengan membuat *constant value* untuk string-string tersebut, seperti yang sudah diimplementasikan di file [`ProductController.java`](https://github.com/hyvos07/pengjut/blob/main/src/main/java/id/ac/ui/cs/advprog/eshop/controller/ProductController.java) versi sekarang.

```java
private static final String SUCCESS = "success";
private static final String ERROR = "error";
```
Dengan begitu, saat kita ingin mengubah isi dari *string* yang dipakai di beberapa tempat tersebut, kita hanya perlu mengubah variabel `SUCCESS` dan `ERROR` saja.


<br>

> Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!

Pada versi yang terbaru (di pengerjaan Modul 2 ini), saya merasa bahwa *project* ini sudah mengimplementasikan apa yang biasanya disebut dengan *Continuous Integration and Continuous Deployment* (CI/CD). Pada *project* ini, saya sudah mengimplementasikan Github Workflow yang akan menjalankan *unit tests* yang saya buat di folder `Test`, yang menunjukkan implementasi CI. Selain *test* yang dibuat manual, terdapat pula Scorecard dan Sonarcloud yang membantu dalam project ini untuk menganalisis dan memberi saran pada kode yang baru saja di-*push* ke Github. Ketiga *tools* ini otomatis dijalankan setelah ada *push* baru yang masuk di Github, membuatnya akan berjalan secara terus-menerus.

Selain CI, terdapat pula CD ke PaaS (*Platform as a Service*) yang akan otomatis melakukan *deployment* dari *branch* `main` ke [Koyeb](https://pengjut.koyeb.app/) setelah *test* yang dijalankan di *pull request* berhasil.. Walaupun saya tidak memakai Github Workflow untuk mengotomisasi *deployment* ini, Koyeb sudah terotomisasi sehingga akan melakukan *build* dan *deployment* sendiri secara otomatis jika terdapat perubahan baru yang di-*push* di Github. Menurut saya, sistem ini sudah mengikuti konsep dari *Continuous Deployment* (CD).

Maka dari itu, menurut saya *project* ini sudah menganut konsep dari CI/CD yang sudah saya atur.

<br>

## Modul 1

### Reflection 1

> You already implemented two new features using Spring Boot. Check again your source code and evaluate the coding standards that you have learned in this module. Write **clean code principles** and **secure coding practices** that have been applied to your code.  If you find any mistake in your source code, please explain how to improve your code.

#### Clean Code Principles

Pada *project* Spring Boot ini, saya sudah mengimplementasikan prinsip-prinsip *clean code* dengan sebaiknya, sehingga kode yang dibuat tidak hanya sekedar berhasil saat dijalankan saja, tetapi juga terjaga keamanan dan *readability*-nya.

Berikut prinsip-prinsip *clean code* yang saya implementasikan pada kode yang saya buat.

- **Meaningful Names** <br>
    Penggunaan nama variable yang deskriptif dan sesuai dengan penggunaannya.

    ```java
    /** 
     * Penggunaan nama variable yang jelas dan deskriptif, 
     * sehingga tidak memerlukan penjelasan lebih lanjut.
    */

    public class Product {
        private String productId;
        private String productName;
        private int productQuantity;
    }
    ```

- **Single Responsibility Principle (SRP)** <br>
    Suatu *method* atau *class* hanya memiliki 1 fungsi dan tujuan, tidak tercampur dengan kegunaan yang lainnya. Tujuan dan fungsi dari setiap *class* dan *method* itu sendiri juga jelas dan tidak ambigu.

    Misalnya pada *project* ini, setiap *class* dan *method* yang dibuat memiliki kegunaan dan *domain*-nya sendiri. Seperti `ProductController` yang berfungsi sebagai jembatan antara pengguna dengan aplikasi, `ProductService` yang mengurus *business logic*, dan `Product` sebagai model dari data yang disimpan.

- **Comments** <br>
    Kode yang saya buat memiliki *comment* yang secukupnya─ tidak mengotori kode itu sendiri, sehingga tidak malah mengurangi *readability* dari kode itu sendiri.

- **Error Handling** <br>
    *Method* yang ada di `ProductController.java`, seperti `updateProductPut()` sudah mengimplementasikan *error handling* yang dapat terjadi lewat proses yang ada di *service*. Dengan begitu, aplikasi web ini tidak akan berhenti atau mengeluarkan *error* yang tidak seharusnya ditampilkan pada pengguna.


    ```java
    @PutMapping("/edit/{id}")
    public String updateProductPut(@ModelAttribute Product product, @PathVariable String id, RedirectAttributes ra) {
        product.setProductId(id);
        try {
            if (product.getProductQuantity() < 0) {
                ra.addFlashAttribute("error", "Alert: Product quantity must be greater than 0!");
                return "redirect:/product/edit/" + id;
            }

            if (product.getProductName().isEmpty()) {
                ra.addFlashAttribute("error", "Alert: Product name cannot be empty!");
                return "redirect:/product/edit/" + id;
            }

            if (!service.update(product)) {
                ra.addFlashAttribute("error", "Alert: Product cannot be updated!");
            } else {
                ra.addFlashAttribute("success", "Alert: Product updated!");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Alert: Product cannot be updated!");
        }

        return "redirect:/product/list";
    }
    ``` 
Walaupun begitu, *project* ini masih belum mengimplementasikan abstraksi pada *class* dan object lainnya, karena *class* `Product` yang digunakan sampai sekarang masih belum final. Oleh karena itu, *class* tersebut masih dalam bentuk *Concrete*. Selain itu, masih ada beberapa *method* di *project* ini yang mengembalikan nilai null, walaupun sudah dilengkapi dengan *error handling* untuk *null value* seperi `try-catch`.


#### Secure Coding

Di dalam *project* ini juga terdapat beberapa implementasi *secure coding* yang memastikan keamanan dari aplikasi web yang ada dan menjaga pengalaman dari pengguna pada aplikasi web ini. Beberapa implementasi yang saya lakukan seperti validasi input dari sisi *client* dan sisi *server* juga sudah saya lakukan di beberapa tempat, kecuali validasi `productQuantity` di bagian pembuatan produk yang harus merupakan bilangan bulat positif.

Namun, prinsip *secure coding* seperti *Authentication* dan *Authorization* belum bisa diimplementasi dengan baik karena masih belum ada pemakaian akun pengguna (*user management*). *Output Data* yang bisa didapat oleh pengguna juga belum melewati proses *encoding*, karena data yang digunakan masih belum bersifat *confidential*. Kedepannya, prinsip-prinsip yang belum sempat diimplementasikan pada project ini akan ditambahkan di tutorial-tutorial selanjutnya.

<br>

### Reflection 2

> After writing the unit test, how do you feel? How many unit tests should be made in a class? How to make sure that our unit tests are enough to verify our program?

Setelah menggunakan *unit test* di dalam *project* ini, saya merasa kode yang sudah saya buat menjadi lebih aman. Lewat *unit test* ini pula, saya menyadari beberapa celah-celah kecil yang bisa terjadi di dalam kode yang sudah saya buat sebelumnya. Untuk kedepannya, apabila saya akan merubah bagian dari kode saya sehingga fungsionalitasnya berubah, saya pun juga merasa terbantu oleh *unit test* yang bisa memeriksa kebenaran dari *logic* atau cara kerja dari kode saya tersebut.

Banyaknya *unit test* yang sebenarnya harus ditambahkan pada *project* ini tergantung dari berapa *use cases* yang bisa dilakukan di dalam aplikasi web itu sendiri. Menurut saya, setiap *class* setidaknya harus memiliki *unit test* yang bisa meng-*cover* semua method di dalamnya, serta aksi-aksi penting seperti *create*, *update*, dan *delete*. Jika terlalu banyak, rentan terjadinya pengulangan *unit test* yang sama tanpa disadari oleh sang *developer*.

Untuk memastikan *unit test* cukup untuk menguji kode yang sudah dibuat, biasanya para *developer* memakai metrik seperti *code coverage*. *Code coverage* dihitung lewat seberapa banyak baris dari *unit testing* yang dapat dilewati berdasarkan besaran persentase. Jika *code coverage* yang dihasilkan menunjukkan angka 100%, berarti semua baris pada *unit test* yang dibuat untuk kode tersebut dapat berjalan dengan lancar. Namun, karena *code coverage* dihitung berdasarkan *unit test* yang (juga) kita buat, maka tidak berarti 100% itu menunjukkan kode kita sudah bebas dari *bug* dan sebagainya. Oleh karena itu, kita juga perlu menggunakan *tools* lain untuk membantu kita lebih yakin.

<br>

> Suppose that after writing the `CreateProductFunctionalTest.java` along with the corresponding test case, you were asked to create another functional test suite that verifies the number of items in the product list. You decided to create a new Java class similar to the prior functional test suites with the same setup procedures and instance variables. What do you think about the cleanliness of the code of the new functional test suite? Will the new code reduce the code quality?

*File* dari *unit test* baru yang memiliki konfigurasi dan prosedur yang persis sama menghasilkan duplikasi kode yang sudah ada, yang dapat mengurangi *cleanliness* dan kualitas kode secara keseluruhan. Hal ini membuat proses mengubah kedua *unit test* yang ada lebih sulit karena harus memperbarui kedua *file* secara manual.

Penulisan kedua *file unit testing* tersebut juga melanggar prinsip DRY (*Don't Repeat Yourself*), dimana seharusnya kedua kode yang serupa tidak perlu diulang-ulang dan dapat disederhanakan menjadi kode yang lebih enak dibaca dan di-*maintain*. Sebaiknya, *functional test suit* tersebut bisa dimasukkan saja langsung ke `CreateProductFunctionalTest.java` karena masih berada di lingkup *test* yang sama.
