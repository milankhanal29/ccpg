import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SentGiftComponent } from './sent-gift.component';

describe('SentGiftComponent', () => {
  let component: SentGiftComponent;
  let fixture: ComponentFixture<SentGiftComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SentGiftComponent]
    });
    fixture = TestBed.createComponent(SentGiftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
