package juvenis.example.tp2.calculator;

import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {
	private final CalculatorService calculatorService;
	public CalculatorController(CalculatorService calculatorService) {
		this.calculatorService = calculatorService;
	}
	
	@PostMapping
	public ResponseEntity<OperationResponse> calculer(@Valid @RequestBody OperationRequest request) {
		OperationResponse response =
		calculatorService.calculer(request);
		URI location = ServletUriComponentsBuilder
		.fromCurrentRequest()
		.path("/{id}")
		.buildAndExpand(response.id())
		.toUri();
		return ResponseEntity
		.created(location)
		.body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OperationResponse> obtenirParId(@PathVariable Long id) {
		OperationResponse response = calculatorService.obtenirParId(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/history")
	public ResponseEntity<Page<OperationResponse>> obtenirHistorique(Pageable pageable) {
		Page<OperationResponse> historique = calculatorService.obtenirHistorique(pageable);
		return ResponseEntity.ok(historique);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<OperationResponse> modifier( @PathVariable Long id,
		@Valid @RequestBody OperationRequest request) {
		OperationResponse response = calculatorService.modifier(id, request);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> supprimer( @PathVariable Long id) {
		calculatorService.supprimer(id);
		return ResponseEntity.noContent().build();
	}
}