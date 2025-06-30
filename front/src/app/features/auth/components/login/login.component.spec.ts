import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';

class MockSessionService {
  login(credentials: { email: string; password: string }) {
    if (credentials.email === 'is.valid@mail.com' && credentials.password === 'isValidPassword') {
      return Promise.resolve(true);
    } else {
      return Promise.reject(new Error('Invalid credentials'));
    }
  }
}

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [{ provide: SessionService, useClass: MockSessionService }],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should login successfully with valid credentials', () => {
    component.form.setValue({ email: 'valid@mail.com', password: 'validPassword' });
    component.submit();
    expect(component.onError).toBeFalsy();
  });

  it('should show an error message for invalid credentials', () => {
    component.form.setValue({ email: 'u', password: 'a' });
    component.submit();
    expect(component.onError).toBeTruthy();
  });
  
  it('should show an error message if required fields are missing', () => {
    component.form.setValue({ email: '', password: '' });
    component.submit();
    expect(component.onError).toBeTruthy();
  });
});
