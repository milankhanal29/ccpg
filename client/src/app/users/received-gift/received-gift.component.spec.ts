import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceivedGiftComponent } from './received-gift.component';

describe('ReceivedGiftComponent', () => {
  let component: ReceivedGiftComponent;
  let fixture: ComponentFixture<ReceivedGiftComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReceivedGiftComponent]
    });
    fixture = TestBed.createComponent(ReceivedGiftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
