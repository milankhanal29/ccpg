import { Component, OnInit } from '@angular/core';
import { PresentationService } from './presentation.service';
import {HeaderService} from "../services/header.service";

@Component({
  selector: 'app-presentation',
  templateUrl: './presentation.component.html',
  styleUrls: ['./presentation.component.css']
})
export class PresentationComponent implements OnInit {
  presentations: any[] = [];
  constructor(private presentationService: PresentationService,private headerService:HeaderService) {}

  ngOnInit(): void {
    this.loadPresentations();

  }

  loadPresentations(): void {
    const userEmail = this.headerService.getRoleAndEmail().email;
    this.presentationService.getPresentationsForUser(userEmail).subscribe(
      (data) => {
        this.presentations = data;
      },
      (error) => {
        console.error('Error fetching presentations:', error);
      }
    );
  }

  downloadPresentation(id: string): void {
    this.presentationService.downloadPresentation(id).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${id}.pptx`; // Adjust extension as needed
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    });
  }
  deletePresentation(id: string): void {
    const confirmDelete = window.confirm('Are you sure you want to delete this presentation?');
    if (confirmDelete) {
      this.presentationService.deletePresentation(id).subscribe({
        next: () => {
          console.log(`Deleted presentation with ID: ${id}`);
          this.loadPresentations();
        },
        error: (err) => {
          console.error(`Error deleting presentation with ID ${id}:`, err);
        }
      });
    }
  }
}
