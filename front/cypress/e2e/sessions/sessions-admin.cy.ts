/// <reference types="cypress" />
describe('Session - detail', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: 'Test Session',
          date: '2025-07-30',
          teacher_id: 2,
          description: 'This is a test session',
          users: [],
          createdAt: '2025-07-21T10:00:00Z',
          updatedAt: '2025-07-22T10:00:00Z',
        },
        {
          id: 2,
          name: 'Second Session',
          date: '2025-08-02',
          teacher_id: 2,
          description: 'This is another test session',
          users: [],
          createdAt: '2025-07-22T10:00:00Z',
          updatedAt: '2025-07-24T10:00:00Z',
        }
      ]
    }).as('getAllSessions');

    cy.loginAsAdmin();

    cy.wait('@getAllSessions');
  });

  it('should click on the Detail button and see session details', () => {
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Test Session',
        date: '2025-07-30',
        teacher_id: 2,
        description: 'This is a test session',
        users: [],
        createdAt: '2025-08-04T12:11:01',
        updatedAt: '2025-08-04T12:11:01',
      },
    }).as('getSession');
    
    cy.intercept('GET', '/api/teacher/2', {
      statusCode: 200,
      body: {
        id: 2,
        lastName: 'Smith',
        firstName: 'Jane',
        createdAt: '2023-01-01', 
        updatedAt: '2023-01-02',
      }
    }).as('getTeacher');

    cy.get('body');
    cy.get('mat-card.item').should('have.length', 2);  
    cy.get('button[mat-raised-button] span').contains('Detail').click();

    cy.url().should('include', '/sessions/detail/1');

    cy.wait('@getSession');
    cy.wait('@getTeacher');

    cy.get('h1').should('contain', 'Test Session');
    cy.get('.mat-card-title').should('contain', 'Test Session');
    cy.get('.mat-card-subtitle').should('contain', 'Jane SMITH');

    cy.wait(3000)

    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200
    }).as('deleteSession');
  
    cy.get('button span').contains('Delete').click();
    cy.wait('@deleteSession');
  
    // Redirection vers la liste
    cy.url().should('include', '/sessions');
  });
});