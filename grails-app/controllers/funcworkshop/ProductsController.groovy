package funcworkshop

import grails.rest.RestfulController

class ProductsController extends RestfulController {
	static responseFormats = ['json', 'xml']
	
    ProductsController() {
        super(Product)
    }
}
