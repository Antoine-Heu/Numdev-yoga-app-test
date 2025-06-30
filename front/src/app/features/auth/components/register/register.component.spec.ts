import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a valid form when all fields are filled correctly', () => {
    component.form.setValue({ email: 'valid@mail.com', firstName: 'Valid', lastName: 'User', password: 'validPassword' });
    component.submit();
    expect(component.onError).toBeFalsy();
  });

  it('should show an error message when form submission fails', () => {
    component.form.setValue({ email: 'invalid', firstName: 'U', lastName: 'U', password: 'a' });
    component.submit();
    expect(component.onError).toBeTruthy();
  });

  it('should have invalid form when required fields are empty', () => {
    component.form.setValue({ email: '', firstName: '', lastName: '', password: '' });
    expect(component.form.valid).toBeFalsy();
  });
});
