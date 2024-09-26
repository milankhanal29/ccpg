package com.milan.codechangepresentationgenerator.admin;
import com.milan.codechangepresentationgenerator.security.auth.role.Role;
import com.milan.codechangepresentationgenerator.shared.message.MessageConstants;
import com.milan.codechangepresentationgenerator.shared.status.Status;
import com.milan.codechangepresentationgenerator.user.entity.User;
import com.milan.codechangepresentationgenerator.user.entity.UserAddress;
import com.milan.codechangepresentationgenerator.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@Configuration
@Slf4j
public class FileReaderClass {
    private static final String filePath = "D:\\healtogether\\AdminInfo.xlsx";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FileReaderClass(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void readXLSfile() {
        try (XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(filePath))) {
            XSSFSheet xssfSheet = work.getSheet("admin_info");
            XSSFRow row =null;
            int i = 1;
            while ((row = xssfSheet.getRow(i)) != null) {
                Optional<User> isExistedAdmin = userRepository.findByRoleAndEmail(Role.ADMIN, getStringCellValue(row.getCell(4)));
                if (isExistedAdmin.isEmpty()) {
                    log.info(MessageConstants.ADMIN_REGISTRATION_IS_PROCEEDING);
                    var adminAddress = UserAddress.builder()
                            .country(getStringCellValue(row.getCell(8)))
                            .city(getStringCellValue(row.getCell(8)))
                            .street(getStringCellValue(row.getCell(9)))
                            .streetNumber(getStringCellValue(row.getCell(10)))
                            .build();
                    User admin = new User();
                    admin.setFirstName(getStringCellValue(row.getCell(1)));
                    admin.setMiddleName(getStringCellValue(row.getCell(2)));
                    admin.setLastName(getStringCellValue(row.getCell(3)));
                    admin.setEmail(getStringCellValue(row.getCell(4)));
                    admin.setPassword(passwordEncoder.encode(getStringCellValue(row.getCell(5))));
                    admin.setPhone(getStringCellValue(row.getCell(6)));
                    admin.setRole(Role.ADMIN);
                    admin.setGender("Male");
                    admin.setUserAddress(adminAddress);
                    admin.setStatus(Status.ACTIVE);
                    userRepository.save(admin);
                    log.info(MessageConstants.ADMIN_SUCCESSFULLY_REGISTERED);
                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }
}
