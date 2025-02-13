# ⚙️ Advanced Programming - Tutorial

### 🛍️ AdvProg Eshop

**Nama**: Daniel Liman <br>
**NPM**: 2306220753 <br>
**Kelas**: A

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
