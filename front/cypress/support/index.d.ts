/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable {
    /**
     * Mocke un utilisateur admin en session
     */
    loginAsAdmin(): Chainable<void>;
    loginAsUser(): Chainable<void>;
  }
}