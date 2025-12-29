-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.44 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 libraryub 的数据库结构
CREATE DATABASE IF NOT EXISTS `libraryub` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `libraryub`;

-- 导出  表 libraryub.admin 结构
CREATE TABLE IF NOT EXISTS `admin` (
  `AdminID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  PRIMARY KEY (`AdminID`),
  UNIQUE KEY `uk_username` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  libraryub.admin 的数据：~1 rows (大约)
INSERT INTO `admin` (`AdminID`, `Username`, `Password`) VALUES
	(1, 'admin', '123456');

-- 导出  表 libraryub.book 结构
CREATE TABLE IF NOT EXISTS `book` (
  `BookID` varchar(20) NOT NULL COMMENT '馆藏号',
  `title` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `Status` varchar(10) NOT NULL DEFAULT '在库',
  `Category` varchar(50) DEFAULT '其他' COMMENT '新增字段',
  `ISBN` varchar(20) DEFAULT NULL COMMENT '新增字段',
  PRIMARY KEY (`BookID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  libraryub.book 的数据：~5 rows (大约)
INSERT INTO `book` (`BookID`, `title`, `author`, `price`, `publisher`, `Status`, `Category`, `ISBN`) VALUES
	('9787208158191', '细说宋史', '虞云国', 98.00, '人民教育出版社', '在库', '荐购新书', '9787208158191'),
	('B001', 'Java编程思想', 'Bruce Eckel', 108.00, '机械工业', '在库', '计算机', '9787111111111'),
	('B002', '三体', '刘慈欣', 38.00, '重庆出版社', '在库', '科幻', '9787222222222'),
	('B003', '活着', '余华', 25.00, '作家出版社', '在库', '文学', '9787333333333'),
	('B004', '小品', '高手', 52.00, '人民教育出版社', '在库', '荐购新书', '9787208158192');

-- 导出  表 libraryub.borrow 结构
CREATE TABLE IF NOT EXISTS `borrow` (
  `BorrowID` int NOT NULL AUTO_INCREMENT,
  `BookID` varchar(20) NOT NULL,
  `CardID` varchar(20) NOT NULL,
  `BorrowTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `DueTime` datetime NOT NULL,
  `ReturnTime` datetime DEFAULT NULL,
  `OverdueDays` int DEFAULT '0',
  `FineAmount` decimal(10,2) DEFAULT '0.00',
  `IsPaid` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`BorrowID`),
  KEY `BookID` (`BookID`),
  KEY `CardID` (`CardID`),
  CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`BookID`) REFERENCES `book` (`BookID`),
  CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`CardID`) REFERENCES `reader` (`CardID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  libraryub.borrow 的数据：~4 rows (大约)
INSERT INTO `borrow` (`BorrowID`, `BookID`, `CardID`, `BorrowTime`, `DueTime`, `ReturnTime`, `OverdueDays`, `FineAmount`, `IsPaid`) VALUES
	(1, 'B003', 'R001', '2025-10-28 13:03:14', '2025-11-29 13:03:14', '2025-12-29 01:21:35', 0, 0.00, 1),
	(2, '9787208158191', 'R001', '2025-10-29 01:24:03', '2025-12-01 01:24:03', '2025-12-29 01:25:29', 28, 14.00, 1),
	(3, 'B004', 'R001', '2025-10-29 02:13:50', '2025-11-28 02:13:50', '2025-12-29 02:14:31', 31, 15.50, 1),
	(4, 'B004', 'R001', '2025-10-29 02:31:50', '2025-11-29 02:31:50', '2025-12-29 02:32:23', 30, 15.00, 1);

-- 导出  存储过程 libraryub.BorrowBook 结构
DELIMITER //
CREATE PROCEDURE `BorrowBook`(
    IN p_BookID VARCHAR(20),  -- 输入：图书ID（如B001）
    IN p_CardID VARCHAR(20)   -- 输入：读者卡号（如R001）
)
BEGIN
    DECLARE vBorrowDays INT DEFAULT 0;
    DECLARE vDueTime DATETIME;
    DECLARE vErrMsg VARCHAR(100); -- 存储错误信息，避免直接CONCAT

    -- 简化异常处理（兼容低版本MySQL，去掉SIGNAL中的CONCAT）
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '执行失败：数据库操作异常（可能是参数错误或数据问题）';
    END;

    -- 验证1：图书是否在库
    IF NOT EXISTS (SELECT 1 FROM Book WHERE BookID = p_BookID AND Status = '在库') THEN
        SET vErrMsg = CONCAT('图书 ', p_BookID, ' 不在库，无法借阅！');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = vErrMsg;
    END IF;

    -- 验证2：读者是否存在
    IF NOT EXISTS (SELECT 1 FROM Reader WHERE CardID = p_CardID) THEN
        SET vErrMsg = CONCAT('读者 ', p_CardID, ' 不存在！');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = vErrMsg;
    END IF;

    -- 验证3：读者是否达最大借阅数量
    IF (SELECT CurrentBorrow FROM Reader WHERE CardID = p_CardID) >= 
       (SELECT MaxBorrow FROM Reader WHERE CardID = p_CardID) THEN
        SET vErrMsg = CONCAT('读者 ', p_CardID, ' 已达最大借阅数量！');
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = vErrMsg;
    END IF;

    -- 获取可借天数，计算应还时间
    SELECT BorrowDays INTO vBorrowDays FROM Reader WHERE CardID = p_CardID;
    SET vDueTime = DATE_ADD(NOW(), INTERVAL vBorrowDays DAY);

    -- 插入借阅记录
    INSERT INTO Borrow (BookID, CardID, BorrowTime, DueTime)
    VALUES (p_BookID, p_CardID, NOW(), vDueTime);

    -- 更新图书状态为"借出"
    UPDATE Book SET Status = '借出' WHERE BookID = p_BookID;

    -- 更新读者借书数量+1
    UPDATE Reader SET CurrentBorrow = CurrentBorrow + 1 WHERE CardID = p_CardID;

    -- 返回成功结果
    SELECT 
        '借书成功' AS 结果,
        p_CardID AS 读者卡号,
        p_BookID AS 图书ID,
        NOW() AS 借书时间,
        vDueTime AS 应还时间,
        vBorrowDays AS 可借天数;

END//
DELIMITER ;

-- 导出  表 libraryub.department 结构
CREATE TABLE IF NOT EXISTS `department` (
  `DeptID` int NOT NULL AUTO_INCREMENT,
  `DeptName` varchar(50) NOT NULL,
  PRIMARY KEY (`DeptID`),
  UNIQUE KEY `uk_DeptName` (`DeptName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  libraryub.department 的数据：~3 rows (大约)
INSERT INTO `department` (`DeptID`, `DeptName`) VALUES
	(2, '文学院'),
	(3, '经管学院'),
	(1, '计算机学院');

-- 导出  表 libraryub.fine 结构
CREATE TABLE IF NOT EXISTS `fine` (
  `FineID` int NOT NULL AUTO_INCREMENT,
  `BorrowID` int NOT NULL,
  `CardID` varchar(20) NOT NULL,
  `Amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `PaidDate` datetime DEFAULT NULL,
  `IsPaid` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`FineID`),
  KEY `fine_ibfk_1` (`BorrowID`),
  KEY `fine_ibfk_2` (`CardID`),
  CONSTRAINT `fine_ibfk_1` FOREIGN KEY (`BorrowID`) REFERENCES `borrow` (`BorrowID`),
  CONSTRAINT `fine_ibfk_2` FOREIGN KEY (`CardID`) REFERENCES `reader` (`CardID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  libraryub.fine 的数据：~1 rows (大约)
INSERT INTO `fine` (`FineID`, `BorrowID`, `CardID`, `Amount`, `PaidDate`, `IsPaid`) VALUES
	(1, 4, 'R001', 15.00, NULL, 0);

-- 导出  表 libraryub.purchase_request 结构
CREATE TABLE IF NOT EXISTS `purchase_request` (
  `RequestID` int NOT NULL AUTO_INCREMENT,
  `ISBN` varchar(20) DEFAULT NULL,
  `Title` varchar(255) NOT NULL,
  `Author` varchar(255) DEFAULT NULL,
  `Publisher` varchar(255) DEFAULT NULL,
  `Price` decimal(10,2) DEFAULT NULL,
  `ReaderID` varchar(20) NOT NULL,
  `RequestDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `Status` varchar(20) NOT NULL DEFAULT '待处理',
  PRIMARY KEY (`RequestID`),
  KEY `fk_purchase_reader` (`ReaderID`),
  CONSTRAINT `fk_purchase_reader` FOREIGN KEY (`ReaderID`) REFERENCES `reader` (`CardID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  libraryub.purchase_request 的数据：~0 rows (大约)
INSERT INTO `purchase_request` (`RequestID`, `ISBN`, `Title`, `Author`, `Publisher`, `Price`, `ReaderID`, `RequestDate`, `Status`) VALUES
	(1, '9787208158191', '细说宋史', '虞云国', '人民教育出版社', 98.00, 'R001', '2025-12-28 05:07:17', '已批准'),
	(2, '9787208158192', '小品', '高手', '人民教育出版社', 52.00, 'R001', '2025-12-29 02:07:21', '已批准');

-- 导出  表 libraryub.reader 结构
CREATE TABLE IF NOT EXISTS `reader` (
  `CardID` varchar(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `Sex` char(1) NOT NULL DEFAULT '男',
  `DeptID` int DEFAULT NULL,
  `Type` varchar(20) DEFAULT '学生',
  `Level` varchar(20) DEFAULT NULL,
  `Password` varchar(255) DEFAULT '123456',
  `MaxBorrow` int DEFAULT '5',
  `BorrowDays` int DEFAULT '30',
  `CurrentBorrow` int DEFAULT '0',
  `IsActive` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`CardID`),
  KEY `DeptID` (`DeptID`),
  CONSTRAINT `reader_ibfk_1` FOREIGN KEY (`DeptID`) REFERENCES `department` (`DeptID`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  libraryub.reader 的数据：~2 rows (大约)
INSERT INTO `reader` (`CardID`, `name`, `Sex`, `DeptID`, `Type`, `Level`, `Password`, `MaxBorrow`, `BorrowDays`, `CurrentBorrow`, `IsActive`) VALUES
	('R001', '张三', '男', 1, '本科生', '大二', '123456', 5, 30, 0, 1),
	('R002', '李四', '女', 2, '研究生', '研一', '123456', 5, 30, 0, 1),
	('R004', '晓明', '男', 2, '教师', '正教授', '123456', 5, 30, 0, 1),
	('R005', '小小', '男', 1, '教师', '正教授', '123456', 5, 30, 0, 1);

-- 导出  存储过程 libraryub.ReturnBook 结构
DELIMITER //
CREATE PROCEDURE `ReturnBook`(
	IN `inputBorrowID` INT
)
BEGIN
    -- 修正：vBookID改为VARCHAR(20)，匹配Book表的BookID类型
    DECLARE vBookID VARCHAR(20);
    DECLARE vCardID VARCHAR(20); -- CardID也是字符串（如R001），同样改VARCHAR
    DECLARE vDueTime DATETIME;
    DECLARE vOverdueDays INT DEFAULT 0;
    DECLARE vFinePerDay DECIMAL(10,2) DEFAULT 0.5;
    DECLARE vFineAmount DECIMAL(10,2) DEFAULT 0;

    -- 异常处理
    DECLARE EXIT HANDLER FOR NOT FOUND
    BEGIN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '错误：未找到有效的借阅记录或书籍已归还。';
    END;

    -- 1. 获取借阅信息（此时vBookID能正确存储B001）
    SELECT b.BookID, b.CardID, b.DueTime
    INTO vBookID, vCardID, vDueTime
    FROM Borrow b
    WHERE b.BorrowID = inputBorrowID AND b.ReturnTime IS NULL;

    -- 2. 更新还书时间
    UPDATE Borrow 
    SET ReturnTime = NOW()
    WHERE BorrowID = inputBorrowID;

    -- 3. 计算逾期天数
    SET vOverdueDays = GREATEST(TIMESTAMPDIFF(DAY, vDueTime, NOW()), 0);

    -- 4. 获取罚款率
    SELECT CAST(sc.ConfigValue AS DECIMAL(10,2))
    INTO vFinePerDay
    FROM SystemConfig sc
    WHERE sc.ConfigKey = 'overdue_fine_rate';
    IF vFinePerDay IS NULL THEN SET vFinePerDay = 0.5; END IF;

    -- 5. 计算罚款&记录（去掉CreateTime字段，匹配fine表结构）
    SET vFineAmount = vOverdueDays * vFinePerDay;
    IF vOverdueDays > 0 THEN
        UPDATE Borrow SET OverdueDays = vOverdueDays, FineAmount = vFineAmount WHERE BorrowID = inputBorrowID;

        -- 修复：fine表没有CreateTime，去掉该字段
        INSERT INTO Fine (BorrowID, CardID, Amount, IsPaid)
        VALUES (inputBorrowID, vCardID, vFineAmount, 0);
    END IF;

    -- 6. 更新图书状态（此时vBookID=B003，能正确匹配记录）
    UPDATE Book 
    SET Status = '在库' 
    WHERE BookID = vBookID;

    -- 7. 更新读者借阅数量
    UPDATE Reader 
    SET CurrentBorrow = CurrentBorrow - 1 
    WHERE CardID = vCardID;

    -- 8. 返回结果
    SELECT 
        '还书成功' AS 结果,
        vBookID AS 图书ID,
        '在库' AS 图书状态,
        vFineAmount AS 罚款金额;
        
END//
DELIMITER ;

-- 导出  表 libraryub.systemconfig 结构
CREATE TABLE IF NOT EXISTS `systemconfig` (
  `ConfigKey` varchar(50) NOT NULL,
  `ConfigValue` varchar(100) DEFAULT NULL,
  `Description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ConfigKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  libraryub.systemconfig 的数据：~2 rows (大约)
INSERT INTO `systemconfig` (`ConfigKey`, `ConfigValue`, `Description`) VALUES
	('max_borrow_days', '30', '最大借阅天数'),
	('overdue_fine_rate', '0.5', '日罚款金额');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
