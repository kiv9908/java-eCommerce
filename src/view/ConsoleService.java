package view;

import domain.model.User;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * 콘솔 기반 입출력을 담당하는 서비스 클래스
 */
public class ConsoleService {
    private final Scanner scanner;
    private final PrintStream out;

    // 의존성 주입을 위한 생성자
    public ConsoleService(InputStream inputStream, PrintStream outputStream) {
        this.scanner = new Scanner(inputStream, "UTF-8");
        this.out = outputStream;
    }

    /**
     * 메시지 출력
     * @param message 출력할 메시지
     */
    public void printMessage(String message) {
        out.println(message);
    }

    /**
     * 에러 메시지 출력
     * @param message 출력할 에러 메시지
     */
    public void printError(String message) {
        out.println("[오류] " + message);
    }

    /**
     * 줄 구분선 출력
     */
    public void printDivider() {
        out.println("----------------------------------------");
    }

    /**
     * 문자열 입력 받기
     * @param prompt 출력할 프롬프트
     * @return 사용자가 입력한 문자열
     */
    public String readLine(String prompt) {
        out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * 정수 입력 받기
     * @param prompt 출력할 프롬프트
     * @return 사용자가 입력한 정수
     */
    public int readInt(String prompt) {
        while (true) {
            try {
                out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                printError("숫자만 입력해주세요.");
            }
        }
    }

    /**
     * 메뉴 출력 및 선택 받기
     * @param title 메뉴 제목
     * @param options 메뉴 옵션 목록
     * @return 선택한 메뉴 번호 (1부터 시작)
     */
    public int showMenu(String title, List<String> options) {
        printDivider();
        printMessage(title);
        printDivider();

        for (int i = 0; i < options.size(); i++) {
            printMessage((i + 1) + ". " + options.get(i));
        }

        printDivider();

        while (true) {
            int choice = readInt("선택하세요: ");
            if (choice >= 1 && choice <= options.size()) {
                return choice;
            } else {
                printError("1부터 " + options.size() + " 사이의 숫자를 입력하세요.");
            }
        }
    }

    /**
     * 확인 메시지 출력 및 응답 받기
     * @param message 확인 메시지
     * @return 확인 여부 (Y/N)
     */
    public boolean confirm(String message) {
        while (true) {
            String response = readLine(message + " (Y/N): ");
            if (response.equalsIgnoreCase("Y")) {
                return true;
            } else if (response.equalsIgnoreCase("N")) {
                return false;
            } else {
                printError("Y 또는 N을 입력하세요.");
            }
        }
    }

    /**
     * 계속하기 위한 대기
     * @param message 출력할 메시지
     */
    public void waitForEnter(String message) {
        readLine(message + " (Enter 키를 누르세요)");
    }

    /**
     * 화면 지우기 시도 (일부 환경에서만 작동)
     */
    public void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                out.print("\033[H\033[2J");
                out.flush();
            }
        } catch (Exception e) {
            // 화면 지우기 실패 시 여러 줄 출력으로 대체
            for (int i = 0; i < 50; i++) {
                out.println();
            }
        }
    }

    /**
     * 사용자 정보 테이블 형식으로 출력
     * @param user 출력할 사용자 객체
     */
    public void displayUserInfo(User user) {
        printDivider();
        printMessage("[ 사용자 정보 ]");
        printDivider();
        printMessage("ID: " + user.getUserId());
        printMessage("이름: " + user.getUserName());
        printMessage("이메일: " + user.getEmail());
        printMessage("전화번호: " + user.getMobileNumber());
        printMessage("상태: " + getStatusText(user.getStatus()));
        printMessage("유형: " + getUserTypeText(user.getUserType()));
        printDivider();
    }

    /**
     * 상태 코드를 텍스트로 변환
     * @param status 상태 코드
     * @return 상태 텍스트
     */
    private String getStatusText(String status) {
        switch (status) {
            case "ST00":
                return "요청";
            case "ST01":
                return "정상";
            case "ST02":
                return "탈퇴";
            default:
                return "알 수 없음";
        }
    }

    /**
     * 사용자 유형 코드를 텍스트로 변환
     * @param userType 사용자 유형 코드
     * @return 사용자 유형 텍스트
     */
    private String getUserTypeText(String userType) {
        switch (userType) {
            case "10":
                return "일반 사용자";
            case "20":
                return "관리자";
            default:
                return "알 수 없음";
        }
    }
}