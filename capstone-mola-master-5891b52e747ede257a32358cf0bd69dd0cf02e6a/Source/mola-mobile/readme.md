**Các bước cài đặt:**
* Mở command line gõ `yarn install` hoặc `npm install` để cài đặt các modules
* Mở máy ảo hoặc cắm cable vào device thật
* Gõ `react-native run-android` để build ứng dụng

**Các lỗi thường gặp**
* Màn hình đỏ: Chụp ảnh màn hình post lên group Capstone để mọi người cùng giải quyết
* Màn hình trắng tinh: Stop server rồi mở command line chạy lần lượt
`adb kill-server`
`adb start-server`
`react-native run-android`

tùy trường hợp có thể phải chạy thêm lệnh `adb reverse tcp:8081 tcp:8081`

* updating...
