import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';
import { NotFoundComponent } from './components/not-found/not-found.component';


describe('AppComponent', () => {
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;
  let mockSessionService: Partial<SessionService>;
  let mockRouter: Partial<Router>;
  let service: SessionService;

  beforeEach(async () => {
    service = new SessionService();
    mockSessionService: {
      logOut: jest.fn(),
    };

    mockRouter: {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter },
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should return the current login state as observable', (done) => {
    const isLoggedSubject = new BehaviorSubject<boolean>(false);
    service['isLoggedSubject'] = isLoggedSubject; // Accès direct pour le test

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      done();
    });

    isLoggedSubject.next(true); // Simuler un changement d'état
  });
  
  it('should be logged to session service', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance as any;
    expect(app.sessionService).toBeDefined();
    expect(app.sessionService.sessionInformation).toBeDefined();
  });

  it('should call session service to check if user is admin', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    const isAdmin = app['sessionService'].sessionInformation?.admin;
    expect(isAdmin).toBe(true);
  });
});
