package funcworkshop

import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Value
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class ProductsControllerFuncSpec extends Specification {

    @Value('${local.server.port}')
    Integer port

    @Shared
    RestBuilder restBuilder = new RestBuilder()

    String getBaseUrl() {
        "http://localhost:${port}"
    }

    def setup() {
    }

    def cleanup() {
        Product.withNewSession {
            Product.list()*.delete(flush: true)
        }
    }

    def "It can get existing products"() {
        given: 'two products'
        Product.withNewSession {
            new Product(name: 'product1', price: 10).save(flush: true, failOnError: true)
            new Product(name: 'product2', price: 20).save(flush: true, failOnError: true)
        }

        when: 'getting the products from the api'
        def response = restBuilder.get baseUrl + '/products'

        then: 'it returns a OK status code'
        response.status == 200

        and: 'the json response contains two products'
        def json = response.json
        json.size() == 2

        and: 'one of the elements is named product1 and has a price of 10'
        def p1 = json.find {
            it.name == 'product1'
        }
        p1
        p1.price == 10

        and: 'one of the elements is named product1 and has a price of 20'
        def p2 = json.find {
            it.name == 'product2'
        }
        p2
        p2.price == 20
    }

    def "It can't get results if there is none in database"() {
        when: 'getting the products from the api'
        def response = restBuilder.get baseUrl + '/products'

        then: 'it returns a OK status code'
        response.status == 200

        and: 'the json response contains no products'
        def json = response.json
        json.size() == 0
    }

    def "It can get an existing product"() {
        given: 'a product'
        def product = new Product(name: 'product', price: 10)
        Product.withNewSession {
            product.save(flush: true, failOnError: true)
        }

        when: 'getting the product from the api'
        def response = restBuilder.get baseUrl + "/products/${product.id}"

        then: 'it returns a OK status code'
        response.status == 200

        and: 'the json response contains the product information'
        def json = response.json
        json.name == 'product'
        json.price == 10
    }

    def "It can't get a non-existing product"() {
        when: 'getting the product from the api'
        def response = restBuilder.get baseUrl + "/products/1234"

        then: 'it doesn\'t return a OK status code'
        response.status == 404
    }

    def "It should create a product successfully"() {
        expect:
        Product.withNewSession {
            !Product.findByName(name)
        }

        when:
        def response = restBuilder.post baseUrl + '/products', {
            json([
                    name: name,
                    price: price
            ])
        }

        then:
        response.status == 201

        and:
        Product.withNewSession {
            Product.findByName(name)
        }

        where:
        name = 'name'
        price = 10
    }

    def "It shouldn't create a product giving wrong arguments"() {
        expect:
        Product.withNewSession {
            !Product.findByName(name)
        }

        when:
        def response = restBuilder.post baseUrl + '/products', {
            json([
                    name: name,
                    price: price
            ])
        }

        then:
        response.status != 200

        and:
        Product.withNewSession {
            !Product.findByName(name)
        }

        where:
        name = ''
        price = 10
    }

    def "It should update a product"() {
        expect:
        def product = new Product(name: name, price: 10)
        Product.withNewSession {
            product.save(flush: true, failOnError: true)
        }

        when:
        def response = restBuilder.put baseUrl + '/products/' + product.id, {
            json([
                    name: newName,
                    price: price
            ])
        }

        then:
        response.status == 200

        and:
        Product.withNewSession {
            Product.findByName(newName)
        }

        and:
        response.json.name == newName

        where:
        name = 'product'
        newName = 'product2'
        price = 10
    }

    def "It shouldn't update a product giving wrong arguments"() {
        expect:
        def product = new Product(name: name, price: 10)
        Product.withNewSession {
            product.save(flush: true, failOnError: true)
        }

        when:
        def response = restBuilder.put baseUrl + '/products/' + product.id, {
            json([
                    name: '',
                    price: price
            ])
        }

        then:
        response.status != 200

        and:
        Product.withNewSession {
            Product.findByName(name)
        }

        where:
        name = 'name'
        price = 10
    }

    def "It can delete a product"() {
        expect:
        def product = new Product(name: name, price: 10)
        Product.withNewSession {
            product.save(flush: true, failOnError: true)
        }

        when:
        def response = restBuilder.delete baseUrl + '/products/' + product.id

        then:
        response.status == 204

        and:
        Product.withNewSession {
            !Product.findByName(name)
        }

        where:
        name = 'name'
        price = 10
    }
}