USE capstone_project;

-- tạo dữ liệu bảng role
INSERT INTO role(role_name) VALUES ('user');
INSERT INTO role(role_name) VALUES ('owner');
INSERT INTO role(role_name) VALUES ('staff');

-- tạo dữ liệu trong bảng field_type
INSERT INTO field_type(name, number_player) VALUES('5 vs 5', 5);
INSERT INTO field_type(name, number_player) VALUES('7 vs 7', 7);

-- tạo profile
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Bình An', 'Phạm Văn Chiêu, 9, Gò Vấp, Hồ Chí Minh, Vietnam', '0862722830', '106.6496422', '10.8461419', 'http://bongda.choithethao.vn/wp-content/uploads/2016/10/san-bong-da-binh-loi-656x365.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Cây Trâm', '40/3T Phường 9 Gò Vấp, Phạm Văn Chiêu, Phường 9, Gò Vấp, Hồ Chí Minh, Vietnam', '0866595964', '106.6477271', '10.8461419', 'http://ibebiz.blob.core.windows.net/webs/trumconhantao/images/content/2cec7cf6-c7f4-4dc2-a681-2cd8321d69fb-0.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Sài Gòn FC Quận 12', '12/ Tổ 12 Kp1, Tân Chánh Hiệp, Hồ Chí Minh, Vietnam', '84962690274', '106.6294474', '10.8503425', 'http://vietnamfutsal.com/wp-content/uploads/2017/01/anhminhhoa2-500x270.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân bóng Phát Đạt', '441 Tân Chánh Hiệp 26, Tân Chánh Hiệp, Quận 12, Hồ Chí Minh, Vietnam', '0982151049', '106.6173496', '10.8627467', 'https://i.ytimg.com/vi/sy6hcUmbsnY/maxresdefault.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Dạ Phi Cơ', 'Tây Thạnh, Tân Phú, Ho Chi Minh, Vietnam', '0911412417', '106.5588653', '10.8893076', 'https://upvinalo.com/11072016/vinalo-1468237946dWZW8Jjt.jpeg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Chế Lan Viên', 'Đường D13, Tây Thạnh, Hồ Chí Minh, Vietnam', '+84 90 709 08 79', '106.6115749', '10.8121146', 'https://lh5.googleusercontent.com/p/AF1QipOwPF4-Ie3Xmej-cRah6xmAfKUc2xRxpIhP7AEX=w408-h306-k-no', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Mini Hiệp Phát', '64 Lê Trọng Tấn, Tây Thạnh, Tân Phú, Hồ Chí Minh, Vietnam', '+84 28 6657 1391', '106.6277716', '10.8064551', 'https://lh5.googleusercontent.com/p/AF1QipM3x2BY2-AeOuKC2dqBeGGZYn3sx9gH4-_SqlLA=w408-h229-k-no', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Cụm Sân Bóng Đá Mini Sport Plus', 'Đường Kênh 19 Tháng 5, Sơn Kỳ, Tân Phú, Hồ Chí Minh, Vietnam', '+84 93 385 85 79', '106.6114011', '10.7981287', 'http://vietnamfutsal.com/wp-content/uploads/2017/01/san-sport-plus_vietnam-futsal.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Mini Thăng Long', '2 Phan Thúc Duyện, Phường 4, Tân Bình, Hồ Chí Minh, Vietnam', '84961955673', '106.6613902', '10.8037656', 'http://sanchoi.com.vn/wp-content/uploads/2016/10/san20520nguoi.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Hoàng Hoa Thám', 'Phường 12, Tân Bình, Hồ Chí Minh, Vietnam', '0647541238', '106.6484728', '10.8051044', 'http://capkeosaigon.com/upload/news/IMG_6455_(Medium).jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng đá mini Chảo Lửa', '30 Phan Thúc Duyện, Phường 4, Tân Bình, Hồ Chí Minh, Vietnam', '+841227700000', '106.6588568', '10.8056415', 'https://i.ytimg.com/vi/QHvJRtlA11Y/maxresdefault.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Mini Hoàng Gia', '4, Trường Chinh, Phường 15, Quận Tân Bình, Phường 15, Tân Bình, Hồ Chí Minh, Vietnam', '084 92 586 56 86', '106.6333581', '10.8004286', 'http://17h30.com/Images/Stadium/2016/3/21/10655168_584523935056615_9080897515748431679_o.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('CFC Soccer School', '22, Trương Quyền,, Q.3,, Thành Phố Hồ Chí Minh, phường 6, Quận 3, Hồ Chí Minh, Vietnam', '084 28 3781 0381', '106.6727601', '10.789214', 'https://i.ytimg.com/vi/h4NTdZdjC0Y/maxresdefault.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân vận động Hoa Lư', '2 Đường Đinh Tiên Hoàng, Đa Kao, Quận 1, Hồ Chí Minh, Vietnam', '0978145123', '106.6727601', '10.789214', 'https://www.google.com.vn/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwjJkJj7wPTWAhWIK48KHZkrCLEQjBwIBA&url=http%3A%2F%2Fstatic.panoramio.com%2Fphotos%2Foriginal%2F11258074.jpg&psig=AOvVaw0peiN0OD1j4E0GIotgBwFI&ust=1508221456393804', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Nguyen Du Football Yard', '116 Nguyễn Du, Bến Thành, Quận 1, Hồ Chí Minh, Vietnam', '084 28 3827 3865', '106.6727601', '10.789214', 'https://www.google.com.vn/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&ved=0ahUKEwjgm82cwfTWAhWCJJQKHSjqDkAQjBwIBA&url=http%3A%2F%2Fsanbongconhantao.vn%2Fimage%2Fdata%2Fnam-dinh.jpg&psig=AOvVaw0XV6hYyJaPYRV7J4ZuhhAx&ust=1508221537743129', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Nguyễn Tri Phương', 'F2 bis Đồng Nai, Cư xá Bắc Hải, Phường 15, Quận 10, Hồ Chí Minh, F2 bis Đồng Nai, Cư xá Bắc Hải, Phường 15, Hồ Chí Minh, Vietnam', '0123456799', '106.6646542', '10.7935446', 'http://gosport.vn/place_images/fb55d2f307ed905/san-bong-bach-viet-2.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Vận Động Long Xuyên', 'Long Hưng, phường 7, Hồ Chí Minh, Vietnam', '', '106.6450455', '10.782774', 'https://i.ytimg.com/vi/0SHoXcTC8Ms/maxresdefault.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Mini Thành Phát', '4, Lê Đại Hành, Phường 15, Quận 11, phường 15, Quận 11, Hồ Chí Minh, Vietnam', '+84 28 3884 0839', '106.6293208', '10.687081', 'http://thegioiconhantao.com.vn/wp-content/uploads/2016/10/cac-giai-doan-phat-trien-co-nhan-tao-1-1024x768.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Đại học FPT', 'Công viên phần mềm Quang Trung, Tân Chánh Hiệp, Quận 12, Hồ Chí Minh', '84 28 7300 5588', '106.629681', '10.853312', 'http://fpt.edu.vn//wp-content/uploads/2015/04/1655485_720024348089292_1523938618531634710_o.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Mini Nguyễn Thị Định', 'Trường THPT Chuyên Năng Khiếu TDTT Nguyễn Thị Định, Đường Số 41, Phường 16, Quận 8, phường 16, Quận 8, Hồ Chí Minh, Vietnam', '0914333856', '106.6528052', '10.7331285', 'http://www.qisc.com.vn/wp-content/uploads/2016/06/san-bong-da-mini-co-nhan-tao-doc-soi-4-1024x768.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Mini Hóc Môn', 'Đỗ Văn Dậy, Tân Hiệp, Hóc Môn, Hồ Chí Minh, Vietnam', '07899999556', '106.5970663', '10.8305351', 'http://img.baocaobang.vn/Uploaded/baichuyen/2014_05_23/pha%20tranh.jpg', 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`) VALUES ('Sân Bóng Đá Mini Đất Mới', '354 Bình Trị Đông, Bình Tân, Hồ Chí Minh, Vietnam','01244455567', '106.5780451', '10.8183862', 'http://baoduongcanhquan.com/upload/images/hinh/trong%20co/co-nhan-tao-san-bong-da-mini.jpg', 0);

-- tạo profile cho người dùng
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`) VALUES ('Minh Quy FC', 'Dương Quảng Hàm, Gò Vấp, Hồ Chí Minh', '0969377089', '2000');
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`) VALUES ('Huu Thanh Club', 'Tô Ký, Công viên phần mềm Quang Trung', '0973337269', '2000');
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`) VALUES ('Huan Messi', 'Phường 7, Tân Phú, Hồ Chí Minh', '0977477877', '2000');
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`) VALUES ('Hieu Map Manucians', 'Tân Kỳ Tân Quý, Tân Bình, Hồ Chí Minh', '0951151049', '2000');
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`) VALUES ('Sai Gon Pro', 'Cây Trâm, Gò Vấp, Hồ Chí Minh', '0932137455', '2000');
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`) VALUES ('RMIT FC', 'Nguyễn Văn Linh, Quận 7, Hồ Chí Minh', '0913131215', '2000');

-- tạo account cho người dùng
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('quymm', '123456', '23', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('thanhth', '123456', '24', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('huanpm', '123456', '25', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('hiepnc', '123456', '26', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('ronaldo77', '123456', '27', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('rmit', '123456', '28', '1', 0);

-- tạo account cho chủ sân
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('binhan', '123456', '1', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('caytram', '123456', '2', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('saigonfc', '123456', '3', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('phatdat', '123456', '4', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('daphico', '123456', '5', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('chelanvien', '123456', '6', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('hiepphat', '123456', '7', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('minisport', '123456', '8', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('thanglong', '123456', '9', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('hoanghoatham', '123456', '10', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('chaolua', '123456', '11', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('hoanggia', '123456', '12', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('soccerschool', '123456', '13', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('hoalu', '123456', '14', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('nguyendu', '123456', '15', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('ntriphuong', '123456', '16', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('longxuyen', '123456', '17', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('thanhphat', '123456', '18', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('dhfpt', '123456', '19', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('nthidinh', '123456', '20', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('hocmon', '123456', '21', '2', 0);
INSERT INTO `capstone_project`.`account` (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES ('datmoi', '123456', '22', '2', 0);




