package controller;

import domain.model.Product;
import service.AuthService;
import service.ProductService;
import view.ConsoleService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductController {
    private final ProductService productService;
    private final ConsoleService consoleService;
    private final AuthService authService;

    // 생성자 주입
    public ProductController(ProductService productService, ConsoleService consoleService, AuthService authService) {
        this.productService = productService;
        this.consoleService = consoleService;
        this.authService = authService;
    }

    // 관리자: 상품 등록
    public void registerProduct() {
        consoleService.printMessage("\n===== 상품 등록 =====");

        try {
            // 상품 정보 입력 받기
//            String productCode = consoleService.readLine("상품 코드(30자 이내): ");
            String productName = consoleService.readLine("상품명(200자 이내): ");
            String detailExplain = consoleService.readLine("상품 설명: ");
            
            // 가격 입력 및 검증
            int salePrice = consoleService.readInt("판매가(숫자만 입력): ");
            if (salePrice < 0) {
                consoleService.printError("판매가는 0 이상이어야 합니다.");
                return;
            }
            
            Integer customerPrice = null;
            String customerPriceStr = consoleService.readLine("선택사항입니다. 입력을 원하지 않으면 Enter를 눌러주세요. \n소비자가(숫자만 입력): ");
            if (!customerPriceStr.isEmpty()) {
                try {
                    customerPrice = Integer.parseInt(customerPriceStr);
                    if (customerPrice < 0) {
                        consoleService.printError("소비자가는 0 이상이어야 합니다.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    consoleService.printError("숫자 형식이 올바르지 않습니다.");
                    return;
                }
            }
            
            // 재고 입력 및 검증
            int stock = consoleService.readInt("재고 수량(숫자만 입력): ");
            if (stock < 0) {
                consoleService.printError("재고는 0 이상이어야 합니다.");
                return;
            }
            
            // 판매 기간 입력(YYYYMMDD 형식)
            String startDate = consoleService.readLine("선택사항입니다. 입력을 원하지 않으면 Enter를 눌러주세요. \n판매 시작일(YYYYMMDD 형식, 선택 사항): ");
            String endDate = consoleService.readLine("선택사항입니다. 입력을 원하지 않으면 Enter를 눌러주세요. \n판매 종료일(YYYYMMDD 형식, 선택 사항): ");
            
            // 이미지 파일 ID
//            String fileId = consoleService.readLine("이미지 파일 ID(선택 사항): ");
            
            // 배송비
            Integer deliveryFee = null;
            String deliveryFeeStr = consoleService.readLine("배송비(선택 사항, 숫자만 입력): ");
            if (!deliveryFeeStr.isEmpty()) {
                try {
                    deliveryFee = Integer.parseInt(deliveryFeeStr);
                    if (deliveryFee < 0) {
                        consoleService.printError("배송비는 0 이상이어야 합니다.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    consoleService.printError("숫자 형식이 올바르지 않습니다.");
                    return;
                }
            }
            
            // 상품 객체 생성
            Product product = new Product();
//            product.setProductCode(productCode);
            product.setProductName(productName);
            product.setDetailExplain(detailExplain);
            product.setSalePrice(salePrice);
            product.setCustomerPrice(customerPrice);
            product.setStock(stock);
            product.setStartDate(startDate.isEmpty() ? null : startDate);
            product.setEndDate(endDate.isEmpty() ? null : endDate);
//            product.setFileId(fileId.isEmpty() ? null : fileId);
            product.setDeliveryFee(deliveryFee);
            
            // 등록자 정보 및 등록 일자
            product.setRegisterId(authService.getCurrentUser().getUserId());
            product.setFirstDate(new Date());
            
            // 상품 등록
            productService.registerProduct(product);
            consoleService.printMessage("상품이 성공적으로 등록되었습니다.");
            
        } catch (NumberFormatException e) {
            consoleService.printError("숫자 형식이 올바르지 않습니다.");
        } catch (Exception e) {
            consoleService.printError("상품 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 관리자: 상품 수정
    public void updateProduct() {
        consoleService.printMessage("\n===== 상품 수정 =====");
        
        try {
            // 상품 코드 입력 받기
            String productCode = consoleService.readLine("수정할 상품 코드: ");
            
            // 상품 조회
            Product product = productService.getProductByCode(productCode);
            if (product == null) {
                consoleService.printError("해당 상품이 존재하지 않습니다.");
                return;
            }
            
            // 현재 상품 정보 출력
            consoleService.printMessage("현재 상품 정보:");
            consoleService.printMessage("상품명: " + product.getProductName());
            consoleService.printMessage("판매가: " + product.getSalePrice());
            consoleService.printMessage("재고: " + product.getStock());
            
            // 수정할 정보 입력 받기
            String newName = consoleService.readLine("새 상품명(변경 없으면 Enter): ");
            if (!newName.isEmpty()) {
                product.setProductName(newName);
            }
            
            String newDetailExplain = consoleService.readLine("새 상품 설명(변경 없으면 Enter): ");
            if (!newDetailExplain.isEmpty()) {
                product.setDetailExplain(newDetailExplain);
            }
            
            String newSalePriceStr = consoleService.readLine("새 판매가(변경 없으면 Enter): ");
            if (!newSalePriceStr.isEmpty()) {
                try {
                    Integer newSalePrice = Integer.parseInt(newSalePriceStr);
                    if (newSalePrice < 0) {
                        consoleService.printError("판매가는 0 이상이어야 합니다.");
                        return;
                    }
                    product.setSalePrice(newSalePrice);
                } catch (NumberFormatException e) {
                    consoleService.printError("숫자 형식이 올바르지 않습니다.");
                    return;
                }
            }
            
            String newStockStr = consoleService.readLine("새 재고 수량(변경 없으면 Enter): ");
            if (!newStockStr.isEmpty()) {
                try {
                    Integer newStock = Integer.parseInt(newStockStr);
                    if (newStock < 0) {
                        consoleService.printError("재고는 0 이상이어야 합니다.");
                        return;
                    }
                    product.setStock(newStock);
                } catch (NumberFormatException e) {
                    consoleService.printError("숫자 형식이 올바르지 않습니다.");
                    return;
                }
            }
            
            // 상품 수정
            productService.updateProduct(product);
            consoleService.printMessage("상품이 성공적으로 수정되었습니다.");
            
        } catch (Exception e) {
            consoleService.printError("상품 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 관리자: 상품 삭제
    public void deleteProduct() {
        consoleService.printMessage("\n===== 상품 삭제 =====");
        
        try {
            // 상품 코드 입력 받기
            String productCode = consoleService.readLine("삭제할 상품 코드: ");
            
            // 상품 조회
            Product product = productService.getProductByCode(productCode);
            if (product == null) {
                consoleService.printError("해당 상품이 존재하지 않습니다.");
                return;
            }
            
            // 삭제 확인
            boolean confirmed = consoleService.confirm("정말로 '" + product.getProductName() + "' 상품을 삭제하시겠습니까?");
            if (confirmed) {
                boolean success = productService.deleteProduct(productCode);
                if (success) {
                    consoleService.printMessage("상품이 성공적으로 삭제되었습니다.");
                } else {
                    consoleService.printError("상품 삭제에 실패했습니다.");
                }
            } else {
                consoleService.printMessage("상품 삭제가 취소되었습니다.");
            }
            
        } catch (Exception e) {
            consoleService.printError("상품 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 관리자 & 일반 사용자: 상품 목록 조회
    public void listProducts() {
        consoleService.printMessage("\n===== 상품 목록 =====");

        try {
            // 정렬 방식 선택 (가격 오름차순 또는 내림차순)
            List<String> options = new ArrayList<>();
            options.add("가격 낮은순");
            options.add("가격 높은순");
            int choice = consoleService.showMenu("정렬 방식", options);

            boolean ascending = (choice == 1); // 1: 오름차순, 2: 내림차순

            // 정렬된 상품 목록 조회
            List<Product> products = productService.getProductsSortedByPrice(ascending);

            if (products.isEmpty()) {
                consoleService.printMessage("등록된 상품이 없습니다.");
                return;
            }

            // 상품 목록 출력 (상태 정보 추가)
            String headerFormat = "%-12s %-43s %-15s %-10s %-10s";
            String dataFormat = "%-12s %-43s %-15s %-10d %-10s";
            consoleService.printMessage(String.format(headerFormat,
                "상품코드", "상품명", "가격", "재고", "상태"));

            // 구분선 출력
            consoleService.printMessage("-------------------------------------------------------------------------------------");

            for (Product product : products) {
                // 상품명 처리 (길이가 40자 이상인 경우 자르고 ... 추가)
                String productName = product.getProductName();
                if (productName.length() > 40) {
                    productName = productName.substring(0, 37) + "...";
                }

                // 가격 표시 형식 지정 (천 단위 콤마)
                String formattedPrice = String.format("%,d", product.getSalePrice());

                // 상품 상태 가져오기
                String status = "판매중";
                if (product.getStock() <= 0) {
                    status = "품절";
                } else if (product.getStartDate() != null && product.getEndDate() != null) {
                    // 판매 기간 체크
                    Date currentDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String today = sdf.format(currentDate);

                    if (product.getStartDate().compareTo(today) > 0) {
                        status = "판매예정";
                    } else if (product.getEndDate().compareTo(today) < 0) {
                        status = "판매종료";
                    }
                }

                consoleService.printMessage(String.format(dataFormat,
                    product.getProductCode(), 
                    productName, 
                    formattedPrice, 
                    product.getStock(),
                    status));
            }

        } catch (Exception e) {
            consoleService.printError("상품 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 관리자 & 일반 사용자: 상품 상세 조회
    public void viewProductDetail() {
        consoleService.printMessage("\n===== 상품 상세 정보 =====");
        
        try {
            // 상품 코드 입력 받기
            String productCode = consoleService.readLine("상품 코드: ");
            
            // 상품 조회
            Product product = productService.getProductByCode(productCode);
            if (product == null) {
                consoleService.printError("해당 상품이 존재하지 않습니다.");
                return;
            }
            
            // 상품 상세 정보 출력 (상태 정보 추가)
            consoleService.printDivider();
            consoleService.printMessage("상품 코드: " + product.getProductCode());
            consoleService.printMessage("상품명: " + product.getProductName());
            consoleService.printMessage("상품 설명: " + product.getDetailExplain());
            consoleService.printMessage("판매가: " + product.getSalePrice() + "원");
            
            if (product.getCustomerPrice() != null) {
                consoleService.printMessage("소비자가: " + product.getCustomerPrice() + "원");
            }
            
            consoleService.printMessage("재고: " + product.getStock() + "개");
            
            if (product.getDeliveryFee() != null) {
                consoleService.printMessage("배송비: " + product.getDeliveryFee() + "원");
            }
            
            if (product.getStartDate() != null && product.getEndDate() != null) {
                consoleService.printMessage("판매 기간: " + product.getStartDate() + " ~ " + product.getEndDate());
            }
            
            // 상품 상태 표시 추가
            consoleService.printMessage("상품 상태: " + product.getProductStatus());
            consoleService.printDivider();
            
        } catch (Exception e) {
            consoleService.printError("상품 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 관리자: 재고 관리
    public void manageStock() {
        consoleService.printMessage("\n===== 재고 관리 =====");
        
        try {
            // 상품 코드 입력 받기
            String productCode = consoleService.readLine("재고를 관리할 상품 코드: ");
            
            // 상품 조회
            Product product = productService.getProductByCode(productCode);
            if (product == null) {
                consoleService.printError("해당 상품이 존재하지 않습니다.");
                return;
            }
            
            // 현재 재고 출력
            consoleService.printMessage("현재 재고: " + product.getStock() + "개");
            
            // 재고 직접 설정
            int newStock = consoleService.readInt("새 재고 수량: ");
            if (newStock < 0) {
                consoleService.printError("재고는 0 이상이어야 합니다.");
                return;
            }
            
            // 재고 업데이트
            boolean success = productService.updateProductStock(productCode, newStock);
            if (success) {
                consoleService.printMessage("재고가 성공적으로 업데이트되었습니다. 새 재고: " + newStock + "개");
            } else {
                consoleService.printError("재고 업데이트에 실패했습니다.");
            }
            
        } catch (NumberFormatException e) {
            consoleService.printError("숫자 형식이 올바르지 않습니다.");
        } catch (Exception e) {
            consoleService.printError("재고 관리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 관리자: 판매 상태 관리
    public void manageSaleStatus() {
        consoleService.printMessage("\n===== 판매 상태 관리 =====");
        
        try {
            // 상품 코드 입력 받기
            String productCode = consoleService.readLine("판매 상태를 관리할 상품 코드: ");
            
            // 상품 조회
            Product product = productService.getProductByCode(productCode);
            if (product == null) {
                consoleService.printError("해당 상품이 존재하지 않습니다.");
                return;
            }
            
            // 현재 판매 상태 출력
            consoleService.printMessage("판매 시작일: " + (product.getStartDate() != null ? product.getStartDate() : "설정되지 않음"));
            consoleService.printMessage("판매 종료일: " + (product.getEndDate() != null ? product.getEndDate() : "설정되지 않음"));
            
            // 판매 상태 수정 방식 선택
            List<String> options = new ArrayList<>();
            options.add("판매 기간 설정");
            options.add("판매 중지 처리");
            options.add("판매 재개");
            int choice = consoleService.showMenu("판매 상태 수정 방식", options);
            
            boolean success = false;
            
            switch (choice) {
                case 1: // 판매 기간 설정
                    String startDate = consoleService.readLine("판매 시작일(YYYYMMDD 형식): ");
                    String endDate = consoleService.readLine("판매 종료일(YYYYMMDD 형식): ");
                    
                    success = productService.updateProductSaleStatus(productCode, startDate, endDate);
                    if (success) {
                        consoleService.printMessage("판매 기간이 성공적으로 업데이트되었습니다.");
                    } else {
                        consoleService.printError("판매 기간 업데이트에 실패했습니다.");
                    }
                    break;
                    
                case 2: // 판매 중지 처리
                    success = productService.suspendProductSale(productCode);
                    if (success) {
                        consoleService.printMessage("상품 판매가 중지되었습니다.");
                    } else {
                        consoleService.printError("판매 중지 처리에 실패했습니다.");
                    }
                    break;
                    
                case 3: // 판매 재개
                    // 현재 날짜를 시작일로, 1년 후를 종료일로 설정
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Calendar calendar = Calendar.getInstance();
                    String today = dateFormat.format(calendar.getTime());
                    
                    calendar.add(Calendar.YEAR, 1);
                    String nextYear = dateFormat.format(calendar.getTime());
                    
                    success = productService.updateProductSaleStatus(productCode, today, nextYear);
                    if (success) {
                        consoleService.printMessage("상품 판매가 재개되었습니다.");
                    } else {
                        consoleService.printError("판매 재개 처리에 실패했습니다.");
                    }
                    break;
                    
                default:
                    return;
            }
            
        } catch (Exception e) {
            consoleService.printError("판매 상태 관리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 관리자: 품절 처리
    public void markProductOutOfStock() {
        consoleService.printMessage("\n===== 품절 처리 =====");

        try {
            // 상품 코드 입력 받기
            String productCode = consoleService.readLine("품절 처리할 상품 코드: ");

            // 상품 조회
            Product product = productService.getProductByCode(productCode);
            if (product == null) {
                consoleService.printError("해당 상품이 존재하지 않습니다.");
                return;
            }

            // 현재 재고 출력
            consoleService.printMessage("현재 재고: " + product.getStock() + "개");

            // 품절 확인
            boolean confirmed = consoleService.confirm("정말로 이 상품을 품절 처리하시겠습니까?");
            if (confirmed) {
                // 품절 처리
                boolean success = productService.markProductAsOutOfStock(productCode);
                if (success) {
                    consoleService.printMessage("상품이 성공적으로 품절 처리되었습니다.");
                } else {
                    consoleService.printError("품절 처리에 실패했습니다.");
                }
            } else {
                consoleService.printMessage("품절 처리가 취소되었습니다.");
            }

        } catch (Exception e) {
            consoleService.printError("품절 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 관리자 & 일반 사용자: 상품명으로 검색
    public void searchProductsByName() {
        consoleService.printMessage("\n===== 상품명 검색 =====");
        
        try {
            // 검색할 상품명 입력 받기
            String searchKeyword = consoleService.readLine("검색할 상품명: ");
            
            if (searchKeyword.trim().isEmpty()) {
                consoleService.printError("검색어를 입력해주세요.");
                return;
            }
            
            // 상품명으로 검색
            List<Product> products = productService.searchProductsByName(searchKeyword);
            
            if (products.isEmpty()) {
                consoleService.printMessage("검색 결과가 없습니다.");
                return;
            }
            
            // 검색 결과 출력
            String headerFormat = "%-12s %-43s %-15s %-10s %-10s";
            String dataFormat = "%-12s %-43s %-15s %-10d %-10s";
            consoleService.printMessage(String.format(headerFormat, 
                "상품코드", "상품명", "가격", "재고", "상태"));
            
            // 구분선 출력
            consoleService.printMessage("-------------------------------------------------------------------------------------");
            
            for (Product product : products) {
                // 상품명 처리 (길이가 40자 이상인 경우 자르고 ... 추가)
                String productName = product.getProductName();
                if (productName.length() > 40) {
                    productName = productName.substring(0, 37) + "...";
                }
                
                // 가격 표시 형식 지정 (천 단위 콤마)
                String formattedPrice = String.format("%,d", product.getSalePrice());
                
                // 상품 상태 가져오기
                String status = product.getProductStatus();
                
                consoleService.printMessage(String.format(dataFormat,
                    product.getProductCode(), 
                    productName, 
                    formattedPrice, 
                    product.getStock(),
                    status));
            }
            
            // 검색 결과 수 표시
            consoleService.printMessage("\n총 " + products.size() + "개의 상품이 검색되었습니다.");
            
        } catch (Exception e) {
            consoleService.printError("상품 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}