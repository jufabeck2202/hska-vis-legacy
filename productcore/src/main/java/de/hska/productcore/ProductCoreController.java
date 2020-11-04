package de.hska.productcore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCoreController {
	@RequestMapping(value = "/oracle/", method = RequestMethod.GET)
	public ResponseEntity<String> getProduct() {
		return new ResponseEntity<String>("beju ist besser im kicker als tobi", HttpStatus.OK);
	}
}
