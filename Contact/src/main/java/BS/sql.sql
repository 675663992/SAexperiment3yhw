USE contactdb;

drop table contacts;
CREATE TABLE contacts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          address VARCHAR(100),
                          phone VARCHAR(20)
);
INSERT INTO contacts (name, address, phone) VALUES
                                                ('杨皓文', '西南石油大学', '13330712563'),
                                                ('yhw', '上海市浦东新区', '202231060613'),
                                                ('John Doe', 'New York, USA', '+1 2125551111');
