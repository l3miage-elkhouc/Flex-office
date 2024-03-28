import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './component/register/register.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { LoginComponent } from './component/login/login.component';
import { AffectationComponent } from './component/affectation/affectation.component';
import { VisualisationComponent } from './component/visualisation/visualisation.component';
import { EquipeComponent } from './component/equipe/equipe.component';
import { BureauComponent } from './component/bureau/bureau.component';

const routes: Routes = [
  { path: "register", component: RegisterComponent },
  { path: "login", component: LoginComponent },
  { path: "dashboard", component: DashboardComponent },
  { path: "affectation", component: AffectationComponent },
  { path: "visualisation", component: VisualisationComponent },
  { path: "equipe", component: EquipeComponent},
  { path: "bureau", component: BureauComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
