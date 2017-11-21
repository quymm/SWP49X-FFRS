USE capstone_project;

-- tạo dữ liệu bảng role
INSERT INTO role(role_name) VALUES ('user');
INSERT INTO role(role_name) VALUES ('owner');
INSERT INTO role(role_name) VALUES ('staff');
INSERT INTO role(role_name) VALUES ('admin');

-- tạo dữ liệu trong bảng field_type
INSERT INTO field_type(name, number_player) VALUES('5 vs 5', 5);
INSERT INTO field_type(name, number_player) VALUES('7 vs 7', 7);


-- tạo profile cho chủ sân
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Bình An', 'Phạm Văn Chiêu, 9, Gò Vấp, Hồ Chí Minh, Vietnam', '0862722830', '106.6496422', '10.8461419', 'http://bongda.choithethao.vn/wp-content/uploads/2016/10/san-bong-da-binh-loi-656x365.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Cây Trâm', '40/3T Phường 9 Gò Vấp, Phạm Văn Chiêu, Phường 9, Gò Vấp, Hồ Chí Minh, Vietnam', '0866595964', '106.6477271', '10.8461419', 'http://ibebiz.blob.core.windows.net/webs/trumconhantao/images/content/2cec7cf6-c7f4-4dc2-a681-2cd8321d69fb-0.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Sài Gòn FC Quận 12', '12/ Tổ 12 Kp1, Tân Chánh Hiệp, Hồ Chí Minh, Vietnam', '84962690274', '106.6294474', '10.8503425', 'http://vietnamfutsal.com/wp-content/uploads/2017/01/anhminhhoa2-500x270.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân bóng Phát Đạt', '441 Tân Chánh Hiệp 26, Tân Chánh Hiệp, Quận 12, Hồ Chí Minh, Vietnam', '0982151049', '106.6173496', '10.8627467', 'https://i.ytimg.com/vi/sy6hcUmbsnY/maxresdefault.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Dạ Phi Cơ', 'Tây Thạnh, Tân Phú, Ho Chi Minh, Vietnam', '0911412417', '106.5588653', '10.8893076', 'https://upvinalo.com/11072016/vinalo-1468237946dWZW8Jjt.jpeg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Chế Lan Viên', 'Đường D13, Tây Thạnh, Hồ Chí Minh, Vietnam', '+84 90 709 08 79', '106.6115749', '10.8121146', 'https://lh5.googleusercontent.com/p/AF1QipOwPF4-Ie3Xmej-cRah6xmAfKUc2xRxpIhP7AEX=w408-h306-k-no', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Mini Hiệp Phát', '64 Lê Trọng Tấn, Tây Thạnh, Tân Phú, Hồ Chí Minh, Vietnam', '+84 28 6657 1391', '106.6277716', '10.8064551', 'https://lh5.googleusercontent.com/p/AF1QipM3x2BY2-AeOuKC2dqBeGGZYn3sx9gH4-_SqlLA=w408-h229-k-no', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Cụm Sân Bóng Đá Mini Sport Plus', 'Đường Kênh 19 Tháng 5, Sơn Kỳ, Tân Phú, Hồ Chí Minh, Vietnam', '+84 93 385 85 79', '106.6114011', '10.7981287', 'http://vietnamfutsal.com/wp-content/uploads/2017/01/san-sport-plus_vietnam-futsal.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Mini Thăng Long', '2 Phan Thúc Duyện, Phường 4, Tân Bình, Hồ Chí Minh, Vietnam', '84961955673', '106.6613902', '10.8037656', 'http://sanchoi.com.vn/wp-content/uploads/2016/10/san20520nguoi.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Hoàng Hoa Thám', 'Phường 12, Tân Bình, Hồ Chí Minh, Vietnam', '0647541238', '106.6484728', '10.8051044', 'http://capkeosaigon.com/upload/news/IMG_6455_(Medium).jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng đá mini Chảo Lửa', '30 Phan Thúc Duyện, Phường 4, Tân Bình, Hồ Chí Minh, Vietnam', '+841227700000', '106.6588568', '10.8056415', 'https://i.ytimg.com/vi/QHvJRtlA11Y/maxresdefault.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Mini Hoàng Gia', '4, Trường Chinh, Phường 15, Quận Tân Bình, Phường 15, Tân Bình, Hồ Chí Minh, Vietnam', '084 92 586 56 86', '106.6333581', '10.8004286', 'http://17h30.com/Images/Stadium/2016/3/21/10655168_584523935056615_9080897515748431679_o.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('CFC Soccer School', '22, Trương Quyền,, Q.3,, Thành Phố Hồ Chí Minh, phường 6, Quận 3, Hồ Chí Minh, Vietnam', '084 28 3781 0381', '106.6727601', '10.789214', 'https://i.ytimg.com/vi/h4NTdZdjC0Y/maxresdefault.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân vận động Hoa Lư', '2 Đường Đinh Tiên Hoàng, Đa Kao, Quận 1, Hồ Chí Minh, Vietnam', '0978145123', '106.6727601', '10.789214', 'https://www.google.com.vn/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwjJkJj7wPTWAhWIK48KHZkrCLEQjBwIBA&url=http%3A%2F%2Fstatic.panoramio.com%2Fphotos%2Foriginal%2F11258074.jpg&psig=AOvVaw0peiN0OD1j4E0GIotgBwFI&ust=1508221456393804', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Nguyen Du Football Yard', '116 Nguyễn Du, Bến Thành, Quận 1, Hồ Chí Minh, Vietnam', '084 28 3827 3865', '106.6727601', '10.789214', 'https://www.google.com.vn/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&ved=0ahUKEwjgm82cwfTWAhWCJJQKHSjqDkAQjBwIBA&url=http%3A%2F%2Fsanbongconhantao.vn%2Fimage%2Fdata%2Fnam-dinh.jpg&psig=AOvVaw0XV6hYyJaPYRV7J4ZuhhAx&ust=1508221537743129', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Nguyễn Tri Phương', 'F2 bis Đồng Nai, Cư xá Bắc Hải, Phường 15, Quận 10, Hồ Chí Minh, F2 bis Đồng Nai, Cư xá Bắc Hải, Phường 15, Hồ Chí Minh, Vietnam', '0123456799', '106.6646542', '10.7935446', 'http://gosport.vn/place_images/fb55d2f307ed905/san-bong-bach-viet-2.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Vận Động Long Xuyên', 'Long Hưng, phường 7, Hồ Chí Minh, Vietnam', '', '106.6450455', '10.782774', 'https://i.ytimg.com/vi/0SHoXcTC8Ms/maxresdefault.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Mini Thành Phát', '4, Lê Đại Hành, Phường 15, Quận 11, phường 15, Quận 11, Hồ Chí Minh, Vietnam', '+84 28 3884 0839', '106.6293208', '10.687081', 'http://thegioiconhantao.com.vn/wp-content/uploads/2016/10/cac-giai-doan-phat-trien-co-nhan-tao-1-1024x768.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Đại học FPT', 'Công viên phần mềm Quang Trung, Tân Chánh Hiệp, Quận 12, Hồ Chí Minh', '84 28 7300 5588', '106.629681', '10.853312', 'http://fpt.edu.vn//wp-content/uploads/2015/04/1655485_720024348089292_1523938618531634710_o.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Mini Nguyễn Thị Định', 'Trường THPT Chuyên Năng Khiếu TDTT Nguyễn Thị Định, Đường Số 41, Phường 16, Quận 8, phường 16, Quận 8, Hồ Chí Minh, Vietnam', '0914333856', '106.6528052', '10.7331285', 'http://www.qisc.com.vn/wp-content/uploads/2016/06/san-bong-da-mini-co-nhan-tao-doc-soi-4-1024x768.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Mini Hóc Môn', 'Đỗ Văn Dậy, Tân Hiệp, Hóc Môn, Hồ Chí Minh, Vietnam', '07899999556', '106.5970663', '10.8305351', 'http://img.baocaobang.vn/Uploaded/baichuyen/2014_05_23/pha%20tranh.jpg', 0, 0);
INSERT INTO `capstone_project`.`profile` (`name`, `address`, `phone`, `longitude`, `latitude`, `avatar_url`, `bonus_point`, `balance`) VALUES ('Sân Bóng Đá Mini Đất Mới', '354 Bình Trị Đông, Bình Tân, Hồ Chí Minh, Vietnam','01244455567', '106.5780451', '10.8183862', 'http://baoduongcanhquan.com/upload/images/hinh/trong%20co/co-nhan-tao-san-bong-da-mini.jpg', 0, 0);

-- tạo profile cho người dùng
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Minh Quý FC', 'Dương Quảng Hàm, Gò Vấp, Hồ Chí Minh', '0969377089', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Hữu Thành Club', 'Tô Ký, Công viên phần mềm Quang Trung', '0973337269', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Huấn Messi', 'Phường 7, Tân Phú, Hồ Chí Minh', '0977477877', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Hiếu Manucians', 'Tân Kỳ Tân Quý, Tân Bình, Hồ Chí Minh', '0951151049', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Sài Gòn Pro', 'Cây Trâm, Gò Vấp, Hồ Chí Minh', '0932137455', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('RMIT FC', 'Nguyễn Văn Linh, Quận 7, Hồ Chí Minh', '0913131215', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Hiệp Pig', 'Chu Văn An, Bình Thạnh, Hồ Chí Minh', '0914151049', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Thiện Buffalo', 'Gò Dưa, Tân Phú, Hồ Chí Minh', '0999999999', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Khánh KT', 'Cầu Bình Triệu, Bình Thạnh, Hồ Chí Minh', '012345678', '2000', 0, 500, 0);
INSERT INTO profile (`name`, `address`, `phone`, `rating_score`, `bonus_point`, `balance`, `account_payable`) VALUES ('Tín PT', 'Cây Trâm, Gò Vấp, Hồ Chí Minh', '0913311514', '2000', 0, 500, 0);

-- tạo account cho người dùng
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('quymm', '123456', '23', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('thanhth', '123456', '24', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('huanpm', '123456', '25', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('hiepnc', '123456', '26', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('ronaldo77', '123456', '27', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('rmit', '123456', '28', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('hieppig', '123456', '29', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('thienbuf', '123456', '30', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('khanhkt', '123456', '31', '1', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('tinpt', '123456', '32', '1', 0);


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

-- tạo profile cho staff
INSERT INTO profile (`name`, `address`, `phone`) VALUES ('Nhân viên Thành', 'Nguyễn Ảnh Thủ, quận 12', '0969377089');
INSERT INTO profile (`name`, `address`, `phone`) VALUES ('Nhân viên Hiếu', 'Chợ Cầu, quận Gò Vấp', '0912411344');

-- tạo account cho staff
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('thanh', '123456', '33', '3', 0);
INSERT INTO account (`username`, `password`, `profile_id`, `role_id`, `num_of_report`) VALUES('hieu', '123456', '34', '3', 0);

-- tạo mức giá max min hệ thống
INSERT INTO standard_price (`staff_id`, `field_type_id`, `date_from`, `date_to`, `rush_hour`, `max_price`, `min_price`) VALUES (33, 1, '2017-10-11', '2018-01-01', 1, 300, 150);
INSERT INTO standard_price (`staff_id`, `field_type_id`, `date_from`, `date_to`, `rush_hour`, `max_price`, `min_price`) VALUES (33, 0, '2017-10-11', '2018-01-01', 1, 200, 80);

-- tạo sân cho chủ sân
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Sân 1', 11, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Sân 2', 11, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Sân 3', 11, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Sân 4', 11, 2);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Sân 5', 11, 2);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Etihad', 12, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Old Tranford', 12, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Wembley', 12, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Anfield', 12, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Goodison', 13, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Liberty', 13, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Stamford Bridge', 13, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('White Hart Lane', 13, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Sân XX', 14, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Sân YY', 14, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('San Siro', 15, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Stadio Olimpico', 15, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Stadio San Paolo', 15, 2);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Thống Nhất', 16, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Gò Đậu', 16, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Vinh', 16, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Tam Kỳ', 16, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Hàng Đẫy', 16, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Lạch Tray', 17, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Pleiku', 17, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Unique', 18, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Num 1', 19, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Num 2', 19, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Num 3', 19, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('AAA', 20, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('BBB', 20, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('RonaldoFF', 21, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('MessiFF', 21, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Legend', 21, 2);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Loser', 21, 2);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Victory', 21, 2);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Captain America', 22, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Hulk', 22, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Thor', 22, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Spiderman', 22, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Wonder Woman', 22, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Black Widow', 22, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Black Panther', 22, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Hercules', 22, 2);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Iron Man', 22, 2);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Vision', 22, 2);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Super man', 22, 2);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Lion', 23, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Tiger', 23, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Dragon', 23, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('F100', 24, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('F200', 24, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Italia', 25, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Spain', 25, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Number11', 26, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Number22', 26, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Hà Nội', 27, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Sài Gòn', 27, 1);


INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Angel', 28, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('Demon', 28, 1);


INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('007', 29, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('009', 29, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('008', 29, 1);


INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('C001', 30, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('C002', 30, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('K001', 31, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('K002', 31, 1);

INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('9999', 32, 1);
INSERT INTO `capstone_project`.`field`(`name`, `field_owner_id`, `field_type_id`) values ('8888', 32, 1);

-- tạo time-enable cho chủ sân giờ thường
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(11, 1, 'Sun', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(12, 1, 'Sun', '06:00:00', '17:00:00', 0, 150);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(13, 1, 'Sun', '06:00:00', '17:00:00', 0, 130);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(14, 1, 'Sun', '06:00:00', '17:00:00', 0, 100);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(15, 1, 'Sun', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(16, 1, 'Sun', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(17, 1, 'Sun', '06:00:00', '17:00:00', 0, 130);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(18, 1, 'Sun', '06:00:00', '17:00:00', 0, 140);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(19, 1, 'Sun', '06:00:00', '17:00:00', 0, 140);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(20, 1, 'Sun', '06:00:00', '17:00:00', 0, 140);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(21, 1, 'Sun', '06:00:00', '17:00:00', 0, 110);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(22, 1, 'Sun', '06:00:00', '17:00:00', 0, 110);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(23, 1, 'Sun', '06:00:00', '17:00:00', 0, 110);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(24, 1, 'Sun', '06:00:00', '17:00:00', 0, 110);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(25, 1, 'Sun', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(26, 1, 'Sun', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(27, 1, 'Sun', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(28, 1, 'Sun', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(29, 1, 'Sun', '06:00:00', '17:00:00', 0, 120);
/*
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(30, 1, 'Fri', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(31, 1, 'Fri', '06:00:00', '17:00:00', 0, 120);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(32, 1, 'Fri', '06:00:00', '17:00:00', 0, 120);
*/

-- tạo time-enable cho chủ sân giờ cao điểm
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(11, 1, 'Sun', '17:00:00', '19:00:00', 1, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(11, 1, 'Sun', '19:00:00', '21:00:00', 1, 200);

INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(12, 1, 'Sun', '17:00:00', '19:00:00', 1, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(12, 1, 'Sun', '19:00:00', '21:00:00', 1, 200);

INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(13, 1, 'Sun', '17:00:00', '19:00:00', 1, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(13, 1, 'Sun', '19:00:00', '21:00:00', 1, 200);

INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(14, 1, 'Sun', '17:00:00', '19:00:00', 1, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(14, 1, 'Sun', '19:00:00', '21:00:00', 1, 200);

INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(15, 1, 'Sun', '17:00:00', '19:00:00', 1, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(15, 1, 'Sun', '19:00:00', '21:00:00', 1, 200);

INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(16, 1, 'Sun', '17:00:00', '19:00:00', 1, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(16, 1, 'Sun', '19:00:00', '21:00:00', 1, 200);

INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(17, 1, 'Sun', '17:00:00', '19:00:00', 1, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(17, 1, 'Sun', '19:00:00', '21:00:00', 1, 200);

INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(18, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(19, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(20, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(21, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(22, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(23, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(24, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(25, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(26, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(27, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(28, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `optimal`,`price`) VALUES(29, 1, 'Sun', '17:00:00', '22:00:00', 0, 200);


/*
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `price`) VALUES(30, 1, 'Fri', '17:00:00', '22:00:00', 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `price`) VALUES(31, 1, 'Fri', '17:00:00', '22:00:00', 200);
INSERT INTO time_enable(`field_owner_id`, `field_type_id`, `date_in_week`, `start_time`, `end_time`, `price`) VALUES(32, 1, 'Fri', '17:00:00', '22:00:00', 200);
*/



