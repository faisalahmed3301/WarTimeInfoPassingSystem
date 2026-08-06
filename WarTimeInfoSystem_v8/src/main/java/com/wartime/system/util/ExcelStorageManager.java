package com.wartime.system.util;

import com.wartime.system.model.*;
import com.wartime.system.service.AuthenticationService;
import com.wartime.system.service.GroupService;
import com.wartime.system.service.MessageService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelStorageManager {
    private static final String DATA_DIR = "data" + File.separator + "storage" + File.separator;
    private static final String USERS_FILE = DATA_DIR + "users.xlsx";
    private static final String GROUPS_FILE = DATA_DIR + "groups.xlsx";
    private static final String CHATS_FILE = DATA_DIR + "chats.xlsx";

    public static void saveAllData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        saveUsers();
        saveGroups();
        saveMessages();
    }

    public static void loadAllData() {
        loadUsers();
        loadGroups();
        loadMessages();
    }

    private static void saveUsers() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Name");
            header.createCell(1).setCellValue("Rank");
            header.createCell(2).setCellValue("Password");

            AuthenticationService auth = AuthenticationService.getInstance();
            int rowIdx = 1;
            for (Map.Entry<String, AbstractUser> entry : auth.getUsers().entrySet()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(entry.getValue().getName());
                row.createCell(1).setCellValue(entry.getValue().getRank().name());
                row.createCell(2).setCellValue(auth.getUserCredentials().get(entry.getKey()));
            }

            try (FileOutputStream fileOut = new FileOutputStream(USERS_FILE)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists())
            return;

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheet("Users");
            if (sheet == null)
                return;

            AuthenticationService auth = AuthenticationService.getInstance();
            auth.getUsers().clear();
            auth.getUserCredentials().clear();

            DataFormatter formatter = new DataFormatter();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String name = formatter.formatCellValue(row.getCell(0)).trim();
                String rankStr = formatter.formatCellValue(row.getCell(1)).trim();
                String password = formatter.formatCellValue(row.getCell(2)).trim();

                if (name.isEmpty() || rankStr.isEmpty())
                    continue;
                AbstractUser user = UserFactory.createUser(name, rankStr);
                auth.addUser(user, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveGroups() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Groups");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Name");
            header.createCell(1).setCellValue("Category");
            header.createCell(2).setCellValue("CustomType");
            header.createCell(3).setCellValue("Creator");
            header.createCell(4).setCellValue("DateCreated");
            header.createCell(5).setCellValue("Members");

            GroupService groupService = GroupService.getInstance();
            int rowIdx = 1;
            for (Group group : groupService.getAllGroups()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(group.getName());
                row.createCell(1).setCellValue(group.getCategory().name());
                row.createCell(2).setCellValue(group.getType());
                row.createCell(3).setCellValue(group.getCreator().getName());
                row.createCell(4).setCellValue(group.getDateCreated() != null ? group.getDateCreated().toString() : "");

                StringBuilder members = new StringBuilder();
                for (AbstractUser user : group.getMembers()) {
                    if (members.length() > 0)
                        members.append(",");
                    members.append(user.getName());
                }
                row.createCell(5).setCellValue(members.toString());
            }

            try (FileOutputStream fileOut = new FileOutputStream(GROUPS_FILE)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadGroups() {
        File file = new File(GROUPS_FILE);
        if (!file.exists())
            return;

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheet("Groups");
            if (sheet == null)
                return;

            GroupService groupService = GroupService.getInstance();
            groupService.getAllGroups().clear();

            AuthenticationService auth = AuthenticationService.getInstance();
            DataFormatter formatter = new DataFormatter();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String name = formatter.formatCellValue(row.getCell(0)).trim();
                String catStr = formatter.formatCellValue(row.getCell(1)).trim();
                if (name.isEmpty() || catStr.isEmpty())
                    continue;

                GroupType category = GroupType.valueOf(catStr);
                String customType = formatter.formatCellValue(row.getCell(2)).trim();
                String creatorName = formatter.formatCellValue(row.getCell(3)).trim();
                String dateStr = formatter.formatCellValue(row.getCell(4)).trim();
                String membersStr = formatter.formatCellValue(row.getCell(5)).trim();

                AbstractUser creator = auth.getUser(creatorName);
                if (creator == null)
                    continue;

                java.time.LocalDate dateCreated;
                try {
                    dateCreated = dateStr.isEmpty() ? java.time.LocalDate.now() : java.time.LocalDate.parse(dateStr);
                } catch (java.time.format.DateTimeParseException e) {
                    dateCreated = java.time.LocalDate.now();
                    // If parsing fails, it's likely an old format where dateStr might actually be the start of membersStr
                    if (!dateStr.isEmpty() && membersStr.isEmpty()) {
                        membersStr = dateStr;
                    }
                }
                
                Group group = new Group(name, category, customType, creator, dateCreated);
                for (String mName : membersStr.split(",")) {
                    AbstractUser member = auth.getUser(mName.trim());
                    if (member != null && member != creator) {
                        group.addMember(member);
                    }
                }

                groupService.loadGroup(group);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveMessages() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Messages");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("EncryptedContent");
            header.createCell(1).setCellValue("EncryptionKey");
            header.createCell(2).setCellValue("Strategy");
            header.createCell(3).setCellValue("SenderRank");
            header.createCell(4).setCellValue("TargetRank");
            header.createCell(5).setCellValue("TargetGroup");
            header.createCell(6).setCellValue("TargetUser");
            header.createCell(7).setCellValue("IsRead");
            header.createCell(8).setCellValue("IsEmergency");
            header.createCell(9).setCellValue("Priority");

            MessageService messageService = MessageService.getInstance();
            int rowIdx = 1;
            for (SecureMessage msg : messageService.getAllMessages()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(msg.getEncryptedContent());
                row.createCell(1).setCellValue(msg.getEncryptionKey());
                row.createCell(2).setCellValue(msg.getEncryptionStrategyName());
                row.createCell(3).setCellValue(msg.getSenderRank() != null ? msg.getSenderRank().name() : "");
                row.createCell(4).setCellValue(msg.getTargetRank() != null ? msg.getTargetRank().name() : "");
                row.createCell(5).setCellValue(msg.getTargetGroup() != null ? msg.getTargetGroup().getName() : "");
                row.createCell(6).setCellValue(msg.getTargetUser() != null ? msg.getTargetUser().getName() : "");
                row.createCell(7).setCellValue(msg.isRead());
                row.createCell(8).setCellValue(msg.isEmergency());
                row.createCell(9).setCellValue(msg.getPriority());
            }

            try (FileOutputStream fileOut = new FileOutputStream(CHATS_FILE)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMessages() {
        File file = new File(CHATS_FILE);
        if (!file.exists())
            return;

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheet("Messages");
            if (sheet == null)
                return;

            MessageService messageService = MessageService.getInstance();
            messageService.getAllMessages().clear();

            AuthenticationService auth = AuthenticationService.getInstance();
            GroupService groupService = GroupService.getInstance();
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String encryptedContent = formatter.formatCellValue(row.getCell(0)).trim();
                String encryptionKey = formatter.formatCellValue(row.getCell(1)).trim();
                String strategy = formatter.formatCellValue(row.getCell(2)).trim();

                String senderRankStr = formatter.formatCellValue(row.getCell(3));
                Rank senderRank = senderRankStr.isEmpty() ? null : Rank.valueOf(senderRankStr);

                String targetRankStr = formatter.formatCellValue(row.getCell(4));
                Rank targetRank = targetRankStr.isEmpty() ? null : Rank.valueOf(targetRankStr);

                String targetGroupName = formatter.formatCellValue(row.getCell(5));
                Group targetGroup = targetGroupName.isEmpty() ? null
                        : groupService.getAllGroups().stream().filter(g -> g.getName().equals(targetGroupName))
                                .findFirst().orElse(null);

                String targetUserName = formatter.formatCellValue(row.getCell(6));
                AbstractUser targetUser = targetUserName.isEmpty() ? null : auth.getUser(targetUserName);

                String isReadStr = formatter.formatCellValue(row.getCell(7));
                boolean isRead = Boolean.parseBoolean(isReadStr);

                String isEmergencyStr = formatter.formatCellValue(row.getCell(8));
                boolean isEmergency = Boolean.parseBoolean(isEmergencyStr);

                SecureMessage message = new SecureMessage.Builder()
                        .setEncryptedContent(encryptedContent)
                        .setEncryptionKey(encryptionKey)
                        .setEncryptionStrategyName(strategy)
                        .setSenderRank(senderRank)
                        .setTargetRank(targetRank)
                        .setTargetGroup(targetGroup)
                        .setTargetUser(targetUser)
                        .setRead(isRead)
                        .setEmergency(isEmergency)
                        .setPriority(row.getCell(9) != null ? formatter.formatCellValue(row.getCell(9)).trim() : "NORMAL")
                        .build();

                messageService.getAllMessages().add(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
