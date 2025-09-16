package supportly.supportlybackend.Controller;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import supportly.supportlybackend.Criteria.OrderSC;
import supportly.supportlybackend.Dto.OrderDto;
import supportly.supportlybackend.Model.Client;
import supportly.supportlybackend.Model.Order;
import supportly.supportlybackend.PdfGeneration;
import supportly.supportlybackend.Service.OrderService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "*")
@EnableTransactionManagement
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/search")
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestBody OrderSC criteria) {
        List<OrderDto> orderList = orderService.search(criteria);
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderBody) {
        Order newOrder = orderService.createNewOrder(orderBody);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
//        orderService.deleteOrderById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Order>> findOrderByCompanyName(@RequestParam String companyName) {
        List<Order> orderList = orderService.findOrdersByCompanyName(companyName);
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

//    @PostMapping("/duplicate")
//    public ResponseEntity<Order> createDuplicatedOrder(@RequestBody Long id) {
//        Order duplicateOrder = orderService.duplicateOrderById(id);
//        return new ResponseEntity<>(duplicateOrder, HttpStatus.CREATED);
//    }

//    @GetMapping("/one/{id}")
//    public ResponseEntity<Order> getOneOrder(@PathVariable Long id) {
//        Order order = orderService.findOrderById(id);
//        return new ResponseEntity<>(order, HttpStatus.OK);
//    }

    @PutMapping("/update")
    public ResponseEntity<Order> updateOrder(@RequestBody Order orderBody) {
        Order order = orderService.updateOrder(orderBody);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping(value = "/protocol", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> createOrderProtocol(@RequestBody Order orderBody) throws DocumentException, IOException {
        PdfGeneration pdfGeneration = new PdfGeneration();
        ByteArrayInputStream orderPdf = pdfGeneration.createProtocol(orderBody);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=customers.pdf");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(orderPdf));
    }

    @PostMapping(value = "/invoice", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> createOrderInvoice(@RequestBody Order orderBody) throws DocumentException, IOException {
        PdfGeneration pdfGeneration = new PdfGeneration();
        ByteArrayInputStream orderPdf = pdfGeneration.createInvoice(orderBody);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=customers.pdf");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(orderPdf));
    }
}
