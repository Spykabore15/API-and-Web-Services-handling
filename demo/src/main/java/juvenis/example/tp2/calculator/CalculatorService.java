package juvenis.example.tp2.calculator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import juvenis.example.tp2.exception.ResourceNotFoundException;
import juvenis.example.tp2.exception.ZeroDivisionException;

@Service
@Transactional
public class CalculatorService {
    private final OperationRepository operationRepository;
    public CalculatorService(
            OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
    public OperationResponse calculer(
            OperationRequest request) {
        String operationNormalisee =
                normaliserOperation(request.getOperation());
        Double resultat = effectuerCalcul(
            request.getA(),
            request.getB(),
            operationNormalisee
        );
        Operation operation = new Operation(
            request.getA(),
            request.getB(),
            operationNormalisee,
            resultat
        );
        Operation operationEnregistree =
                operationRepository.save(operation);
        return convertirEnResponse(operationEnregistree);
    }
    @Transactional(readOnly = true)
    public Page<OperationResponse> obtenirHistorique(
            Pageable pageable) {
        return operationRepository
            .findAll(pageable)
            .map(this::convertirEnResponse);
    }
    @Transactional(readOnly = true)
    public OperationResponse obtenirParId(Long id) {
        Operation operation = rechercherOperation(id);
        return convertirEnResponse(operation);
    }
    public OperationResponse modifier(
            Long id,
            OperationRequest request) {
        Operation operationExistante =
                rechercherOperation(id);
        String operationNormalisee =
                normaliserOperation(request.getOperation());
        Double nouveauResultat = effectuerCalcul(
            request.getA(),
            request.getB(),
            operationNormalisee
        );
        operationExistante.setA(request.getA());
        operationExistante.setB(request.getB());
        operationExistante.setOperateur(operationNormalisee);
        operationExistante.setResultat(nouveauResultat);
        Operation operationModifiee = operationRepository.save(operationExistante);
        return convertirEnResponse(operationModifiee);
    }
    public void supprimer(Long id) {
        Operation operation = rechercherOperation(id);
        operationRepository.delete(operation);
    }
    private Operation rechercherOperation(Long id) {
        return operationRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Aucune opération trouvée avec l'identifiant " + id
                )
            );
    }
    private Double effectuerCalcul(
            Double a,
            Double b,
            String operation) {
        return switch (operation) {
            case "addition" -> a + b;
            case "soustraction" -> a - b;
            case "multiplication" -> a * b;
            case "division" -> effectuerDivision(a, b);
            default -> throw new IllegalArgumentException(
                "Opération non prise en charge : " + operation
            );
        };
    }
    private Double effectuerDivision(
            Double a,
            Double b) {
        if (Double.compare(b, 0.0) == 0) {
            throw new ZeroDivisionException(
                "La division par zéro n'est pas autorisée"
            );
        }
        return a / b;
    }
    private String normaliserOperation(
            String operation) {
        if (operation == null) {
            throw new IllegalArgumentException(
                "L'opération est obligatoire"
            );
        }
        return operation.trim().toLowerCase();
    }
    private OperationResponse convertirEnResponse(
            Operation operation) {
        return new OperationResponse(
            operation.getId(),
            operation.getA(),
            operation.getB(),
            operation.getOperateur(),
            operation.getResultat(),
            operation.getDate()
        );
    }
}