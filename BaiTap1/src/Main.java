public static void main(String[] args) {
    List<Book> listBook = new ArrayList<>();
    Scanner x = new Scanner(System.in);
    String msg = """
            "Chương trình quản lý sách\\n"
                        + "1. Thêm 1 cuốn sách\\n"
                        + "2. Xóa 1 cuốn sách\\n"
                        + "3. Thay đổi sách\\n"
                        + "4. Xuất thông tin\\n"
                        + "5. Tìm sách lập trình\\n"
                        + "6. Lấy sách tối đa theo giá\\n"
                        + "7. Tìm kiếm theo tác giả\\n"
                        + "0. Thoát\\n"
                        + "Chọn chức năng:""";


    int chon = 0;
    do {
        System.out.printf(msg);
        chon = x.nextInt();

        switch (chon) {
            case 1 -> {
                Book newBook = new Book();
                newBook.input();
                listBook.add(newBook);
            }
            case 2 -> {
                System.out.print("Nhập vào mã sách cần xóa:");
                int bookid = x.nextInt();
                // kiểm tra mã sách
                Book find = listBook.stream()
                        .filter(p -> p.getId() == bookid)
                        .findFirst()
                        .orElseThrow();
                listBook.remove(find);
                System.out.print("Đã xóa sách thành công \n");
            }
            case 3 -> {
                System.out.print("Nhập vào mã sách cần điều chỉnh:");
                int bookid = x.nextInt();
                Book find = listBook.stream()
                        .filter(p -> p.getId() == bookid)
                        .findFirst()
                        .orElseThrow();
            }

            case 4 -> {
                System.out.println("\n Xuất thông tin danh sách ");
                listBook.forEach(p -> p.output());
            }
            case 5 -> {
                List<Book> list5 = listBook.stream()
                        .filter(u -> u.getTitle().toLowerCase().contains("lập trình"))
                        .toList();

                list5.forEach(Book::output);
            }
            case 6 -> {
                System.out.println("\n Danh sách sách giá cao nhất:");

                listBook.stream()
                        .sorted((a, b) -> Double.compare(b.getPrice(), a.getPrice())) // sắp xếp giảm dần theo giá
                        .limit(3) // lấy tối đa 3 cuốn
                        .forEach(Book::output);
            }


        }
    } while (chon != 0);
}
