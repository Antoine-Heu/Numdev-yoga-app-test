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

        cy.get('mat-card-title').should('contain', 'User information');
        
        cy.get('button[mat-icon-button]').find('mat-icon').should('contain', 'arrow_back');

        // Vérification du nom complet
        cy.get('mat-card-content p').contains('Name: John DOE');

        // Vérification de l'email
        cy.get('mat-card-content p').contains('Email: john.doe@example.com');

        // Vérification du message "Delete my account" (si non admin)
        cy.get('mat-card-content p').contains('Delete my account:');

        // Vérification du bouton "delete"
        cy.get('button[mat-raised-button][color="warn"]').find('mat-icon').should('contain', 'delete');
        cy.get('button[mat-raised-button][color="warn"]').find('span').should('contain', 'Detail');

        // Vérification des dates
        cy.get('mat-card-content p').contains('Create at: June 27, 2025');
        cy.get('mat-card-content p').contains('Last update: June 27, 2025');

        cy.get('button[mat-icon-button]').click();
        cy.url().should('include', '/session');
    });

});