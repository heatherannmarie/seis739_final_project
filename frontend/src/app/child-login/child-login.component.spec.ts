import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChildLoginComponent } from './child-login.component';

describe('ChildLoginComponent', () => {
  let component: ChildLoginComponent;
  let fixture: ComponentFixture<ChildLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChildLoginComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChildLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
