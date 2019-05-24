import { Moment } from 'moment';
import { IClaim } from 'app/shared/model/claim.model';
import { IEnrollment } from 'app/shared/model/enrollment.model';

export interface IPatient {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  birthDate?: Moment;
  age?: number;
  claims?: IClaim[];
  enrollments?: IEnrollment[];
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public birthDate?: Moment,
    public age?: number,
    public claims?: IClaim[],
    public enrollments?: IEnrollment[]
  ) {}
}
