/// <reference types="cypress" />
describe('Sessions - Formulaire', () => {
    beforeEach(() => {
      cy.loginAsAdmin();

      cy.intercept('GET', '/api/session', {
        id: 1,
        name: 'TEST session',
        date: '2024-05-30T13:27:22.000+00:00',
        teacher_id: 1,
        description: 'TEST session',
        users: [2],
        createdAt: '2024-05-29T14:24:33',
        updatedAt: '2024-05-29T15:20:22',
      }).as('getSessions');

      cy.intercept('GET', '/api/teacher', {
        body: [
          { id: 1, firstName: 'John', lastName: 'Doe' },
          { id: 2, firstName: 'Jane', lastName: 'Smith' }
        ]
      }).as('getTeachers')
    });

    it('should click on create button', () => {
      cy.intercept('POST', '/api/session', {
        statusCode: 201,
        body: {
        id: 1,
        name: 'Yoga Session',
        date: '2023-06-01',
        teacher_id: 1,
        description: 'A relaxing yoga session.',
        users: [],
        createdAt: '2023-05-01',
        updatedAt: '2023-05-01'
        }
      }).as('createSession');
  
      cy.get('button[mat-raised-button] span').contains('Create').click();

      cy.url().should('include', '/sessions/create');

      cy.get('input[formControlName=name]').type('Séance test');
      cy.get('input[formControlName=date]').type('2025-07-01');
      cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('John Doe').click();
      cy.get('textarea[formControlName=description]').type('Ceci est un test');
      cy.get('form').submit();

      cy.url().should('include', '/sessions');
      cy.get('.mat-snack-bar-container').should('contain', 'Session created !');
    });
});
