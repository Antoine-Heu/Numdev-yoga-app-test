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
    
        cy.intercept('GET', '/api/teacher', {
            body: [
            { id: 1, lastName: 'Doe', firstName: 'John', createdAt: '2025-07-21T10:00:00Z', updatedAt: '2025-07-21T10:00:00Z' },
            { id: 2, lastName: 'Smith', firstName: 'Jane', createdAt: '2025-07-21T10:00:00Z', updatedAt: '2025-07-21T10:00:00Z' },
            ]
        }).as('getTeachers');
    
        cy.loginAsUser();
    
        cy.wait('@getAllSessions');
    });
  
    it('click on the Detail button and see session details', () => {
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
        }).as('getSessionInitial');
        
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
    
        cy.wait('@getSessionInitial');
        cy.wait('@getTeacher');
    
        cy.get('h1').should('contain', 'Test Session');
        cy.get('.mat-card-title').should('contain', 'Test Session');
        cy.get('.mat-card-subtitle').should('contain', 'Jane SMITH');

        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('participate');

        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
            id: 1,
            name: 'Test Session',
            date: '2025-07-30',
            teacher_id: 2,
            description: 'This is a test session',
            users: [1],
            createdAt: '2025-08-04T12:11:01',
            updatedAt: '2025-08-04T12:11:01',
            },
        }).as('getSessionWithParticipation');

        cy.get('button[mat-raised-button] span').contains('Participate').should('exist').click();
        cy.wait('@participate');
        cy.wait('@getSessionWithParticipation');
      
        cy.intercept('DELETE', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('unParticipate');
        
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
        }).as('getSessionWithUnparticipation');

        cy.get('button[mat-raised-button] span').contains('Do not participate').should('exist').click();

        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('participate');

        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
            id: 1,
            name: 'Test Session',
            date: '2025-07-30',
            teacher_id: 2,
            description: 'This is a test session',
            users: [1],
            createdAt: '2025-08-04T12:11:01',
            updatedAt: '2025-08-04T12:11:01',
            },
        }).as('getSessionWithParticipation');

        cy.get('button[mat-raised-button] span').contains('Participate').should('exist');
    });
  });