/// <reference types="cypress" />
describe('me spec', () => {
    beforeEach(() => {
        cy.loginAsUser()
    });

    it('should click on the "Account" link and retrieve user informations', () => {
        cy.intercept('GET', '/api/user/*', {
            body: {
                    "id": 1,
                    "email": 'john.doe@example.com',
                    "lastName": 'Doe',
                    "firstName": 'John',
                    "admin": false,
                    "createdAt": "2025-06-27T14:53:29",
                    "updatedAt": "2025-06-27T14:53:29"
                },
        }).as('getUser');

        cy.get('span[routerLink="me"]').contains('Account').click();
        
        cy.wait('@getUser');

        cy.intercept('DELETE', '/api/user/1', {
            statusCode: 200,
        }).as('deleteAccount');

        cy.get('button[mat-raised-button][color="warn"]').click();
        cy.get('snack-bar-container').should('contain', 'Your account has been deleted !');
    });
});