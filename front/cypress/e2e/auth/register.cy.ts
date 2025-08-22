/// <reference types="cypress" />
describe('Register spec', () => {
	it('Register successfully', () => {
		cy.visit('/register');
	
		cy.intercept('POST', '/api/auth/register', {
			statusCode: 201,
			body: { message: 'User registered successfully' },
		}).as('register');
	
		cy.get('input[formControlName=firstName]').type('John');
		cy.get('input[formControlName=lastName]').type('Doe');
		cy.get('input[formControlName=email]').type('example@test.com');
		cy.get('input[formControlName=password]').type('test!1234');
	
		cy.get('button[type=submit]').click();
	
		cy.wait('@register').its('request.body').should('deep.equal', {
			firstName: 'John',
			lastName: 'Doe',
			email: 'example@test.com',
			password: 'test!1234',
		});
	
		cy.url().should('include', '/login');
	});
});